package com.springroll.router;

import com.springroll.core.IEvent;
import com.springroll.core.JobStatus;
import com.springroll.core.services.notification.NotificationService;
import com.springroll.notification.CorePushChannels;
import com.springroll.orm.entities.Job;
import com.springroll.orm.repositories.Repositories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by anishjoseph on 15/09/16.
 */
@Service
public class JobManager {
    private final Map<Long, LegMonitor> legMonitorMap = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(JobManager.class);

    @Autowired Repositories repo;
    @Autowired NotificationService notificationService;

    public void reRegisterNewTransactionLeg(Long jobId, Long legId){
        LegMonitor legMonitor = legMonitorMap.get(jobId);
        if(legMonitor != null && legMonitor.refs.get(legId) != null)
            return;

        synchronized (legMonitorMap) {
            legMonitor = legMonitorMap.get(jobId);
            if (legMonitor == null) {
                Job job = repo.job.findOne(jobId);
                legMonitor = new LegMonitor(legId, 0l, job.getStatus());
                legMonitorMap.put(jobId, legMonitor);
            } else {
                Long ref = legMonitor.refs.get(legId);
                if (ref != null) return;
                legMonitor.reAddRef(legId, 0l);
            }
        }
    }
    public Long registerNewTransactionLeg(Long jobId, Long parentLegId){
        Long newLegId = 1l;
        LegMonitor legMonitor = legMonitorMap.get(jobId);
        if(legMonitor == null){
            legMonitor = new LegMonitor();
            synchronized (legMonitorMap){
//                cleanup();
                legMonitorMap.put(jobId, legMonitor);
            }
        } else {
            synchronized (legMonitorMap){
                newLegId = legMonitor.addRef(parentLegId);
            }
        }
        return newLegId;
    }
    public void handleOptimisticLockFailure(Long jobId, Long legId ){
        LegMonitor legMonitor = legMonitorMap.get(jobId);
        Job job = repo.job.findOne(jobId);
        if (legMonitor.jobStatus.length() < 3950) {
            job.setStatus(legMonitor.jobStatus + "Leg" + legId + "-OptLockFail ");
            legMonitor.jobStatus = job.getStatus();
        }
        removeLegsWithParentId(legId, legMonitor, job);
    }

    /*
        When a event is process it may route new events - if any of them are to be delivered in a new
        transaction, then a reference is added in the context of the original event (the new event itself
        will be processed later - after the commit of the current event is done. However if an exception
        occurs before the commit happens, any references registered earlier needs to be cleaned up
     */
    private void removeLegsWithParentId(Long legId, LegMonitor legMonitor, Job job){
        for(Iterator<Map.Entry<Long, Long>> it = legMonitor.refs.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Long, Long> entry = it.next();
            if(entry.getValue().equals(legId)) {
                it.remove();
            }
        }

    }
    public void handleExceptionInTransactionLeg(Long jobId, Long legId, String status){
        if(jobId == null || legId == null){
            logger.debug("In delete reference : Either jobId or legId is null");
            return;
        }
        LegMonitor legMonitor = legMonitorMap.get(jobId);
        Job job = repo.job.findOne(jobId);
        job.setJobStatus(legId == 1 ? JobStatus.FailedInRootTransaction : JobStatus.FailedInSecondaryTransaction);
        removeLegsWithParentId(legId, legMonitor, job);
        removeTransactionLegReference(jobId, legId, status);
    }
    public void removeTransactionLegReference(Long jobId, Long legId, String status){
        if(jobId == null || legId == null){
            logger.debug("In delete reference : Either jobId or legId is null");
            return;
        }
        LegMonitor legMonitor = legMonitorMap.get(jobId);
        if(legMonitor == null){
            logger.debug("No monitor exists for job {} and legId {}", jobId, legId);
            return;
        }
        Job job = repo.job.findOne(jobId);
        synchronized (legMonitorMap) {
            switch (legMonitor.removeRef(legId)) {
                case EMPTIED:
                    job.setCompleted(true);
                    job.setEndTime(LocalDateTime.now());
                    if(job.getJobStatus().equals(JobStatus.InProgress))job.setJobStatus(JobStatus.Success);
                    if (legMonitor.jobStatus.length() < 3950) {
                        job.setStatus(legMonitor.jobStatus + status + "  ");
                        legMonitor.jobStatus = job.getStatus();
                    }
                    logger.debug("Job Completed: Transaction leg {} completed for job {}: Status {}", legId, jobId, job.getStatus());
                    Set<String> users = new HashSet<>(1);
                    users.add(job.getUserId());
                    notificationService.pushNotification(users, new JobStatusMessage(job), CorePushChannels.JOB_STATUS_UPDATE);
                    break;
                case REMOVED:
                    if (legMonitor.jobStatus.length() < 3950) {
                        job.setStatus(legMonitor.jobStatus + status + "  ");
                        legMonitor.jobStatus = job.getStatus();
                    }
                    logger.debug("Transaction leg {} completed for job {}: Status {}", legId, jobId, job.getStatus());
                    break;
                case DOES_NOT_EXIST:
                    break;
            }
        }
        return;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleBeforeCommitEvent(IEvent event) {
        removeTransactionLegReference(event.getJobId(), event.getLegId(), event.getClass().getSimpleName());
    }

    private class LegMonitor {
        private Map<Long, Long> refs = new HashMap<>();
        private Long legId = 1l;
        String jobStatus = "";
        public LegMonitor(){
            refs.put(legId, 0L);
        }
        public LegMonitor(Long legId, Long parentLegId, String jobStatus){
            refs.put(legId, parentLegId);
            this.legId = legId;
            this.jobStatus = jobStatus;
        }

        public Long addRef(Long parentLegId){
            legId = legId + 1;
            refs.put(legId, parentLegId);
            return legId;
        }
        public void reAddRef(Long legId, Long parentLegId){
            if(this.legId < legId) this.legId = legId;
            refs.put(legId, parentLegId);
        }

        public LegStatus removeRef(Long legId){
            Long removedId = refs.remove(legId);
            boolean removed = removedId != null;
            if(!removed)return LegStatus.DOES_NOT_EXIST;
            if(refs.isEmpty())return LegStatus.EMPTIED;
            return LegStatus.REMOVED;
        }
    }
    enum LegStatus {
        DOES_NOT_EXIST,
        EMPTIED,
        REMOVED
    }

}
