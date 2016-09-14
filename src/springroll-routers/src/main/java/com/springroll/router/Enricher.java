package com.springroll.router;

import com.springroll.core.*;
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
@Component
public class Enricher {
    private static final Logger logger = LoggerFactory.getLogger(Enricher.class);

    @Autowired
    DTOMeta dtoMeta;

    public JobMeta on(JobMeta jobMeta){
        DTOEnricher enricherClass = dtoMeta.getEnricherForDTO(jobMeta.getPayloads().get(0));
        if(enricherClass == null){
            logger.debug("No Enricher specified for DTO '{}'", jobMeta.getPayloads().get(0).getClass().getSimpleName());
            return jobMeta;
        }
        for (DTO dto : jobMeta.getPayloads()) {
            enricherClass.enrich(dto, jobMeta.getPrincipal());
        }
        return jobMeta;
    }
}
