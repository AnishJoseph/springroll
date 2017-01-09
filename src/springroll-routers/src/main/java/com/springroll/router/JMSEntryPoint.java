package com.springroll.router;

import com.springroll.core.IEvent;
import com.springroll.core.ContextStore;
import com.springroll.core.ISignallingEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;


/**
 * Created by anishjoseph on 09/09/16.
 */
@Component
public class JmsEntryPoint {
    @Autowired
    ApplicationEventPublisher publisher;
    @Autowired JobManager jobManager;

    public void on(IEvent event) {
        /* Called when the event comes from JMS - it will be in a new thread so set these attributes for the new thread */
        /*  Re-register the job with the monitor. Why?? Remember an event is registered by the job manager in a non-persisted
            job map. Well since the registration happens only in memory imagine a scenario where the event is registed in EventCreator
            and pushed to JMS, after which the the system reboots. On coming up the event (which is in the JMS) will be delivered to
            the asynch side and needs the job map to have the entry for this job - however since the system rebooted the memory map
            will have nothing related to this job. To ensure this we re-register the job. Reregistering will ensure that the job will
            be registered only if its not yet registered
         */
        jobManager.reRegisterNewTransactionLeg(event.getJobId());
        ContextStore.put(event.getUser(), event.getJobId(), event.getLegId());
        publisher.publishEvent(event);

    }

}
