package com.springroll.router;

import com.springroll.core.IEvent;
import com.springroll.orm.entities.Job;
import com.springroll.orm.helpers.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by anishjoseph on 15/09/16.
 */
@Service
public class JobManager {
    private Map<Long, LegMonitor> legMonitorMap = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(JobManager.class);

    @Autowired
    JobRepository jobRepository;

    public Long registerNewTransactionLeg(Long jobId){
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
                newLegId = legMonitor.addRef();
            }
        }
        return newLegId;
    }
    private void removeTransactionLegReference(Long jobId, Long legId, String status){
        if(jobId == null || legId == null){
            logger.debug("In delete reference : Either jobId or legId is null");
            return;
        }
        LegMonitor legMonitor = legMonitorMap.get(jobId);
        if(legMonitor == null){
            logger.debug("No monitor exists for job {} and legId {}", jobId, legId);
            return;
        }
        Job job = jobRepository.findOne(jobId);
        synchronized (legMonitorMap) {
            switch (legMonitor.removeRef(legId)) {
                case EMPTIED:
                    job.setJobDone(true);
                    job.setEndTime(LocalDateTime.now());
                    if (legMonitor.jobStatus.length() < 3950) {
                        job.setStatus(legMonitor.jobStatus + "Leg" + legId + "-" + status + "  ");
                        legMonitor.jobStatus = job.getStatus();
                    }
                    logger.debug("Job Completed: Transaction leg {} completed for job {}: Status {}", legId, jobId, job.getStatus());
                    break;
                case REMOVED:
                    if (legMonitor.jobStatus.length() < 3950) {
                        job.setStatus(legMonitor.jobStatus + "Leg" + legId + "-" + status + "  ");
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
        removeTransactionLegReference(event.getJobId(), event.getLegId(), "");
    }

    private class LegMonitor {
        private Set<Long> refs = new HashSet<>();
        private Long legId = 1l;
        String jobStatus = "";
        public LegMonitor(){
            refs.add(legId);
        }

        public Long addRef(){
            legId = legId + 1;
            refs.add(legId);
            return legId;
        }
        public LegStatus removeRef(Long legId){
            boolean removed = refs.remove(legId);
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
