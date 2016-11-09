package com.springroll.router;

import com.springroll.core.*;
import com.springroll.orm.entities.Job;
import com.springroll.orm.repositories.Repositories;
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
    Repositories repo;

    @Autowired
    JobManager jobManager;

    @Autowired
    ApplicationEventPublisher publisher;

    public Long on(JobMeta jobMeta){
        boolean comingDirectlyFromSyncSide = false;
        Class<? extends IEvent> eventClass = jobMeta.getPayloads().get(0).getProcessor().getServiceFactory().getServiceEvent();
        AbstractEvent event;
        try {
            event = (AbstractEvent) eventClass.newInstance();
        }catch (Exception e){
            logger.error("Unable to instantiate Event class '{}'", eventClass.getSimpleName());
            return -1L;
        }
        event.setPayloads(jobMeta.getPayloads());
        Job job = null;

        //FIXME - java optional?
        boolean needsReview =   jobMeta.getBusinessValidationResults() != null &&
                                !jobMeta.getBusinessValidationResults().getReviewNeededViolations().isEmpty();

        if(jobMeta.getJobId() == null) {
            comingDirectlyFromSyncSide = true;
            job = new Job(jobMeta.getParentJobId(), needsReview, ((ServiceDTO)event.getPayload()).getProcessor().name(), jobMeta.getUser().getUsername(), jobMeta.getPayloads());
            repo.job.save(job);
            jobMeta.setJobId(job.getID());
            if(!needsReview){
                publisher.publishEvent(event);
            }
            ContextStore.put(jobMeta.getUser(), jobMeta.getJobId(), jobManager.registerNewTransactionLeg(jobMeta.getJobId(), 0L));
            jobMeta.setLegId(ContextStore.getLegId());
        }
        event.setJobId(jobMeta.getJobId());
        event.setUser(jobMeta.getUser());
        event.setLegId(jobMeta.getLegId());
        if(!needsReview){
            if (jobMeta.isAsynchronous()) {
                asynchSideEndPoints.routeToJms(event);
            } else {
                asynchSideEndPoints.routeToDynamicRouter(event);
            }
            return event.getJobId();
        }

        /* If we reach here it means that the event needs to be reviewed */

        if(!comingDirectlyFromSyncSide){
            job = repo.job.findOne(event.getJobId());
            job.setUnderReview(true);
        }

        ReviewNeededEvent reviewNeededEvent = new ReviewNeededEvent(event, jobMeta.getBusinessValidationResults().getReviewNeededViolations());
        reviewNeededEvent.setUser(jobMeta.getUser());
        asynchSideEndPoints.routeToJms(reviewNeededEvent);
        return event.getJobId();
    }

}
