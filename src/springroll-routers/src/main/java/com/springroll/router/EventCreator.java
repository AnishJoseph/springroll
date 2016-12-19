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

    /**
     * Assumption : If there are multiple payloads, it is assumed that all payloads belong to the same Service
     * This is the last processor in the route 'Synchronous Route'. This processor is where the pupa becomes a
     * butterfly - the DTO morphs into an Event. The following happens here
     * 1) Based on the service definition of the DTO, an appropriate Event is created and the DTO is encapsulated in the event
     * 2) If the DTO is coming directly from Synchronous Route then there a Job is yet to be created, so create a Job
     * 3) In the newly created event, set the JobId, user, LegId etc
     * 4) If no review is needed for the event route it either via JMS or directly to DynamicROuter (depending on the flag in JobMeta)
     * 5) If the service needs to be reviewed then create a  ReviewNeededEvent and route it via JMS
     *
     * @param jobMeta
     * @return
     */
    public Long on(JobMeta jobMeta){
        boolean comingDirectlyFromSyncSide = false;
        Class<? extends IEvent> eventClass = jobMeta.getPayloads().get(0).getServiceDefinition().getServiceFactory().getServiceEvent();
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

        if(ContextStore.getJobId() == null) {
            comingDirectlyFromSyncSide = true;
            job = new Job(jobMeta.getParentJobId(), needsReview, ((ServiceDTO)event.getPayload()).getServiceDefinition().name(), SpringrollSecurity.getUser().getUsername(), jobMeta.getPayloads());
            repo.job.save(job);
            if(!needsReview){
                publisher.publishEvent(event);
            }
            ContextStore.put(SpringrollSecurity.getUser(), job.getID(), jobManager.registerNewTransactionLeg(job.getID(), 0L));
        }
        event.setJobId(ContextStore.getJobId());
        event.setUser(ContextStore.getUser());
        event.setLegId(ContextStore.getLegId());
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

        ReviewNeededEvent reviewNeededEvent = new ReviewNeededEvent(event, jobMeta.getBusinessValidationResults().getReviewNeededViolations(), SpringrollSecurity.getUser());
        asynchSideEndPoints.routeToJms(reviewNeededEvent);
        return event.getJobId();
    }

}
