package com.springroll.router;

import org.apache.camel.EndpointInject;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by anishjoseph on 09/09/16.
 */
@Component
public class SynchEndPoint {

    private static final Logger logger = LoggerFactory.getLogger(SynchEndPoint.class);

    @EndpointInject(ref = "synchronousEndPoint")
    private ProducerTemplate synchronousEndPoint;

    /* Sends the payload down the Synchronous route - see router.xml route id="Synchronous Route"*/
    public Long route(JobMeta jobMeta){
        return (Long) synchronousEndPoint.sendBody(synchronousEndPoint.getDefaultEndpoint(), ExchangePattern.InOut, jobMeta);
    }

}