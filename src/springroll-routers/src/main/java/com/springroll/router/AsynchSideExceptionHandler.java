package com.springroll.router;

import com.springroll.core.IEvent;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by anishjoseph on 09/09/16.
 */
@Component
public class AsynchSideExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(AsynchSideExceptionHandler.class);

    @Autowired
    DeadLetterQueueHandler deadLetterQueueHandler;
    public void on(Exchange exchange) throws Exception {
        Throwable caused = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
        IEvent problemEvent = (IEvent) exchange.getIn().getBody();
        deadLetterQueueHandler.setExceptionCauses(caused, problemEvent);
    }

}
