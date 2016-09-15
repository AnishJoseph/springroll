package com.springroll.router;

import com.springroll.core.AbstractEvent;
import com.springroll.core.DTOMeta;
import com.springroll.core.IEvent;
import com.springroll.core.UserContextFactory;
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
public class JMSEntryPoint {
    public  static void on(IEvent event) {
        /* Called when the event comes from JMS - it will be in a new thread so set these attributes for the new thread */
        UserContextFactory.setUserContextInThreadScope(event.getPrincipal(), event.getJobId(), event.getLegId());
    }

}
