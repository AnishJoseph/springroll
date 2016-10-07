package com.springroll.router;

import com.springroll.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * The Enricher is one of the <b>processors</b> on the  Synchronous Route.
 * The on method is invoked by camel as the DTO is routed through
 * the Synchronous Route.
 * @author Anish Joseph
 * @since 1.0
 */
@Component
public class Enricher {
    private static final Logger logger = LoggerFactory.getLogger(Enricher.class);

    private Map<Class, DTOEnricher> cache = new HashMap<>();
    @Autowired private ApplicationContext applicationContext;

    /**
     * This is invoked ONLY from camel. Maps the DTO to its enricher and then invokes the enricher
     * @param jobMeta
     * @return
     */
    public JobMeta on(JobMeta jobMeta){
        Class<? extends DTOEnricher> enricherClass = jobMeta.getPayloads().get(0).getProcessor().getEnricherClass();
        if(enricherClass == null){
            logger.debug("No Enricher specified for DTO '{}'", jobMeta.getPayloads().get(0).getClass().getSimpleName());
            return jobMeta;
        }
        DTOEnricher enricher = cache.get(enricherClass);
        if(enricher == null){
            enricher = applicationContext.getBean(enricherClass);
            cache.put(enricherClass, enricher);
        }


        //Fixme - do exception handling
        enricher.enrich(jobMeta.getPayloads());
        return jobMeta;
    }
}
