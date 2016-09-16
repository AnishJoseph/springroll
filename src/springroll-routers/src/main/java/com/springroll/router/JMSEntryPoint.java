package com.springroll.router;

import com.springroll.core.IEvent;
import com.springroll.core.ContextStore;
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

    public void on(IEvent event) {
        /* Called when the event comes from JMS - it will be in a new thread so set these attributes for the new thread */
        ContextStore.put(event.getPrincipal(), event.getJobId(), event.getLegId());
        publisher.publishEvent(event);

    }

}
