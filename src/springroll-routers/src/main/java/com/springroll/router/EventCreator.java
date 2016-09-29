package com.springroll.router;

import com.springroll.core.*;
import com.springroll.orm.entities.Job;
import com.springroll.orm.repositories.JobRepository;
import com.springroll.router.review.ReviewNeededEvent;
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
        Job job = null;
        boolean needsReview =  jobMeta.getBusinessValidationResults() != null && jobMeta.getBusinessValidationResults().getReviewNeededViolations() != null && !jobMeta.getBusinessValidationResults().getReviewNeededViolations().isEmpty();
        if(jobMeta.getJobId() == null) {
            //Fixme - handle signalling events
            comingDirectlyFromSyncSide = true;
            job = new Job();
            job.setPayloads(jobMeta.getPayloads());
            job.setParentId(jobMeta.getParentJobId());
            jobRepository.save(job);
            jobMeta.setJobId(job.getID());
            if(needsReview)job.setStatus("Under Review ");
            ContextStore.put(jobMeta.getUser(), jobMeta.getJobId(), jobManager.registerNewTransactionLeg(jobMeta.getJobId(), 0L));
            jobMeta.setLegId(ContextStore.getLegId());
        }
        event.setJobId(jobMeta.getJobId());
        event.setUser(jobMeta.getUser());
        event.setLegId(jobMeta.getLegId());
        if(jobMeta.getBusinessValidationResults() == null || jobMeta.getBusinessValidationResults().getReviewNeededViolations() == null || jobMeta.getBusinessValidationResults().getReviewNeededViolations().isEmpty()){
            if (jobMeta.isAsynchronous()) {
                asynchSideEndPoints.routeToJms(event);
            } else {
                if (comingDirectlyFromSyncSide) {
                    publisher.publishEvent(event);
                }
                asynchSideEndPoints.routeToDynamicRouter(event);
            }
            return event.getJobId();
        }
        if(!comingDirectlyFromSyncSide){
            job = jobRepository.findOne(event.getJobId());
            job.setStatus(job.getStatus() + " Under Review ");
        }
        ReviewNeededEvent reviewNeededEvent = new ReviewNeededEvent(event, jobMeta.getBusinessValidationResults().getReviewNeededViolations());
        asynchSideEndPoints.routeToJms(reviewNeededEvent);
        return event.getJobId();

    }

}
