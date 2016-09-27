package com.springroll.router;

import com.springroll.router.exceptions.BusinessValidationException;
import com.springroll.router.exceptions.PropertyValidationException;
import org.apache.camel.EndpointInject;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.processor.validation.PredicateValidationException;
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
        try {
            return (Long) synchronousEndPoint.sendBody(synchronousEndPoint.getDefaultEndpoint(), ExchangePattern.InOut, jobMeta);
        }catch (Exception e){
            /* When we throw BusinessValidationException, PropertyValidationException camel encapsulates it in a camel exception - so get root cause */
            if(e.getCause() instanceof PropertyValidationException) throw (PropertyValidationException)e.getCause();
            if(e.getCause() instanceof BusinessValidationException) throw (BusinessValidationException)e.getCause();
            throw e;
        }
    }

}