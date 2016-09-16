package com.springroll.router;

import com.springroll.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


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

    /**
     * This is invoked ONLY from camel. Maps the DTO to its enricher and then invokes the enricher
     * @param jobMeta
     * @return
     */
    public JobMeta on(JobMeta jobMeta){
        //Fixme - do exception handling
        DTOEnricher enricherClass = JobDefinitions.getEnricherForDTO(jobMeta.getPayloads().get(0));
        if(enricherClass == null){
            logger.debug("No Enricher specified for DTO '{}'", jobMeta.getPayloads().get(0).getClass().getSimpleName());
            return jobMeta;
        }
        enricherClass.enrich(jobMeta.getPayloads());
        return jobMeta;
    }
}
