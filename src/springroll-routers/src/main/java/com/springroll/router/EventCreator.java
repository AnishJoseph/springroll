package com.springroll.router;

import com.springroll.core.*;
import com.springroll.orm.entities.Job;
import com.springroll.orm.helpers.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Created by anishjoseph on 09/09/16.
 */
@Component
public class EventCreator {
    private static final Logger logger = LoggerFactory.getLogger(EventCreator.class);

    @Autowired
    AsynchSideEndPoints asynchSideEndPoints;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    JobManager jobManager;

    @Autowired
    ApplicationEventPublisher publisher;

    public Long on(JobMeta jobMeta){
        boolean comingDirectlyFromSyncSide = false;
        Class<? extends IEvent> eventClass = JobDefinitions.getEventForDTO(jobMeta.getPayloads().get(0));
        AbstractEvent event;
        try {
            event = (AbstractEvent) eventClass.newInstance();
        }catch (Exception e){
            logger.error("Unable to instantiate Event class '{}'", eventClass.getSimpleName());
            return -1L;
        }
        event.setPayloads(jobMeta.getPayloads());
        if(jobMeta.getJobId() == null) {
            comingDirectlyFromSyncSide = true;
            Job job = new Job();
            job.setPayloads(jobMeta.getPayloads());
            job.setParentId(jobMeta.getParentJobId());
            jobRepository.save(job);
            jobMeta.setJobId(job.getID());
            ContextStore.put(jobMeta.getUser(), jobMeta.getJobId(), jobManager.registerNewTransactionLeg(jobMeta.getJobId()));
            jobMeta.setLegId(ContextStore.getLegId());
        }
        event.setJobId(jobMeta.getJobId());
        event.setUser(jobMeta.getUser());
        event.setLegId(jobMeta.getLegId());
        if(jobMeta.isAsynchronous()) {
            asynchSideEndPoints.routeToJms(event);
        }else {
            if(comingDirectlyFromSyncSide){
                publisher.publishEvent(event);
            }
            asynchSideEndPoints.routeToDynamicRouter(event);
        }
        return event.getJobId();
    }

}
