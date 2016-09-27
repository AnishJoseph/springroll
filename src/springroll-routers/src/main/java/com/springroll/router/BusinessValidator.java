package com.springroll.router;

import com.springroll.core.*;
import com.springroll.router.exceptions.BusinessValidationException;
import com.springroll.router.exceptions.PropertyValidationException;
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
public class BusinessValidator {
    private static final Logger logger = LoggerFactory.getLogger(BusinessValidator.class);

    /**
     * This is invoked ONLY from camel. Maps the DTO to its enricher and then invokes the enricher
     * @param jobMeta
     * @return
     */
    public JobMeta on(JobMeta jobMeta){
        DTOBusinessValidator validator = JobDefinitions.getBusinessValidatorForDTO(jobMeta.getPayloads().get(0));
        if(validator == null){
            logger.debug("No Business Validator specified for DTO '{}'", jobMeta.getPayloads().get(0).getClass().getSimpleName());
            return jobMeta;
        }
        BusinessValidationResults businessValidationResults = new BusinessValidationResults();
        validator.validate(jobMeta.getPayloads(), businessValidationResults, jobMeta.isBusinessValidationAlreadyDoneOnce());
        if(!businessValidationResults.getBusinessViolations().isEmpty()){
                throw new BusinessValidationException(businessValidationResults.getBusinessViolations());
        }
        if(!businessValidationResults.getOverrideableViolations().isEmpty()){
                throw new BusinessValidationException(businessValidationResults.getOverrideableViolations());
        }
        jobMeta.setBusinessValidationResults(businessValidationResults);
        return jobMeta;
    }
}
