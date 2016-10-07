package com.springroll.router;

import com.springroll.core.DTO;
import com.springroll.router.exceptions.PropertyValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.HashMap;
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
     * This is invoked ONLY from camel. Invokes the JSR349 bean validation. If there is a violation then a
     * PropertyValidationException is thrown.
     * NOTE: The method will go thru all the payloads and collect violations for each of the payloads.
     * The PropertyValidationException will contain a Map<String,Map<String,String>> holding the violations.
     * The Key of the outer map is the index of the payload and the values is the set of violations for that payload.
     * The key of the inner map is the field name that had the constraint violation and the value is the error.
     * An example json of a violations looks like
     * <p>{
            "0": {
                    "fld5": "may not be null",
                    "fld6": "may not be null",
                    "fld7": "must be greater than or equal to 10"
            },
            "1": {
                    "fld1": "may not be null",
                    "fld2": "must be less that 20",
                 }
             }
     </p>
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
        if(!validationExceptions.isEmpty()){
            throw new PropertyValidationException(validationExceptions);
        }
        return jobMeta;
    }
}
