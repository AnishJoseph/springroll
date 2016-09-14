package com.springroll.router;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by anishjoseph on 09/09/16.
 */
@Component
public class AsynchSideExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(AsynchSideExceptionHandler.class);

    public void on(Exchange exchange) throws Exception {
        logger.debug("Received Exception on Asynch Side");
    }

}
