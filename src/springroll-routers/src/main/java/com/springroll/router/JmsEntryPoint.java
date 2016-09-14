package com.springroll.router;

import com.springroll.core.IEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by anishjoseph on 09/09/16.
 */
@Component
public class JmsEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(JmsEntryPoint.class);
    public void on(IEvent x){
        logger.debug("In JMS Entry Point");
    }

}
