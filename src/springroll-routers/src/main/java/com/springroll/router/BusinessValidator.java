package com.springroll.router;

import com.springroll.core.*;
import com.springroll.router.exceptions.BusinessValidationException;
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
public class BusinessValidator {
    private static final Logger logger = LoggerFactory.getLogger(BusinessValidator.class);

    private Map<Class, DTOBusinessValidator> cache = new HashMap<>();

    @Autowired private ApplicationContext applicationContext;

    /**
     * This is invoked ONLY from camel. Maps the DTO to its enricher and then invokes the enricher
     * @param jobMeta
     * @return
     */
    public JobMeta on(JobMeta jobMeta){

        Class<? extends DTOBusinessValidator> validatorClass = jobMeta.getPayloads().get(0).getProcessor().getBusinessValidatorClass();
        if(validatorClass == null){
            logger.debug("No Business Validator specified for DTO '{}'", jobMeta.getPayloads().get(0).getClass().getSimpleName());
            return jobMeta;
        }
        DTOBusinessValidator validator = cache.get(validatorClass);
        if(validator == null)  {
            //FIXME - what if the bean does not exist
            validator = applicationContext.getBean(validatorClass);
            cache.put(validatorClass, validator);
        }

        BusinessValidationResults businessValidationResults = new BusinessValidationResults();
        validator.validate(jobMeta.getPayloads(), businessValidationResults);
        if(!businessValidationResults.getBusinessViolations().isEmpty()){
                throw new BusinessValidationException(businessValidationResults.getBusinessViolations());
        }

        jobMeta.setBusinessValidationResults(businessValidationResults);
        return jobMeta;
    }
}
