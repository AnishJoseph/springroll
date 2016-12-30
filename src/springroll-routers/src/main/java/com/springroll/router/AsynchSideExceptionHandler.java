package com.springroll.router;

import com.springroll.core.IEvent;
import com.springroll.core.exceptions.ExceptionStore;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * The on method is directly invoked by camel when an exception is encountered on the asynchronous side.
 * Since the method is invoked in the same thread as the exception nothing is done here as the transaction
 * that its is running has already rolled back. Instead we store the Throwable and the event that was being
 * processed for processing later in the DeadLetterQueueHandler.
 * <b>Note:</b> Due to the exception the transactions  is rolled back and the event that originated in JMS
 * is pushed to the DeadLetterQueue by ActiveMQ.
 */
@Component
public class AsynchSideExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(AsynchSideExceptionHandler.class);

    public void on(Exchange exchange) throws Exception {
        Throwable caused = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
        IEvent problemEvent = (IEvent) exchange.getIn().getBody();
        ExceptionStore.setExceptionCauses(caused, problemEvent);
    }

}
