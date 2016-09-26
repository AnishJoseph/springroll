package com.springroll.router;

import com.springroll.core.DTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * The Validator is one of the <b>processors</b> on the  Synchronous Route and invokes the JSR bean validation
 * The on method is invoked by camel as the DTO is routed through the Synchronous Route.
 *
 * @author Anish Joseph
 * @since 1.0
 */
@Component
public class PropertyValidator {
    private static final Logger logger = LoggerFactory.getLogger(PropertyValidator.class);

    @Autowired
    private Validator validator;
    /**
     * This is invoked ONLY from camel. Invokes the JSR349 bean validation
     * @param jobMeta
     * @return             .
     */
    public JobMeta on(JobMeta jobMeta){
        int payloadIndex = 0;
        Map<String,Map<String,String>> validationExceptions = new HashMap();
        for (DTO dto : jobMeta.getPayloads()) {
            Set<ConstraintViolation<DTO>> violations = validator.validate(dto);
            if (!violations.isEmpty()) {
                Map<String, String> errors = new HashMap();
                for (ConstraintViolation<DTO> constraintViolation : violations) {
                    String field = constraintViolation.getPropertyPath().toString();
                    errors.put(field, constraintViolation.getMessage());
                }
                validationExceptions.put(Integer.toString(payloadIndex),errors);
            }

            payloadIndex++;
        }
        return jobMeta;
    }
}
