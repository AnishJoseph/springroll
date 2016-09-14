package com.springroll.router;

import com.springroll.core.*;
import com.springroll.orm.entities.Job;
import com.springroll.orm.helpers.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by anishjoseph on 09/09/16.
 */
@Component
public class EventCreator {
    private static final Logger logger = LoggerFactory.getLogger(EventCreator.class);

    @Autowired
    AsynchSideEndPoints asynchSideEndPoints;

    @Autowired
    DTOMeta dtoMeta;

    @Autowired
    JobRepository jobRepository;

    @PersistenceContext
    EntityManager em;


    public Long on(JobMeta jobMeta){
        Class<? extends IEvent> eventClass = dtoMeta.getEventForDTO(jobMeta.getPayloads().get(0));
        AbstractEvent event;
        try {
            event = (AbstractEvent) eventClass.newInstance();
        }catch (Exception e){
            logger.error("Unable to instantiate Event class '{}'", eventClass.getSimpleName());
            return new Long(-1);
        }
        event.setPayloads(jobMeta.getPayloads());
        Job job = null;
        if(jobMeta.getJobId() == null) {
            job = new Job();
            job.setPayloads(jobMeta.getPayloads());
            job.setParentId(jobMeta.getParentJobId());
            jobRepository.save(job);
            event.setJobId(job.getID());
        } else {
            event.setJobId(jobMeta.getJobId());
        }
        if(jobMeta.isAsynchronous()) {
            asynchSideEndPoints.routeWithNewTransaction(event);
        }else {
            asynchSideEndPoints.routeInSameTransaction(event);
        }
        return event.getJobId();
    }

}
