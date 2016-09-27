package com.springroll.router.exceptions;

import com.springroll.core.BusinessValidationResult;

import java.util.List;
import java.util.Map;

/**
 * Created by anishjoseph on 26/09/16.
 */
public class BusinessValidationException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    private List<BusinessValidationResult>  violations;

    public BusinessValidationException(List<BusinessValidationResult> violations) {
        super("Business Rule Violation");
        this.violations = violations;
    }

    public List<BusinessValidationResult> getViolations() {
        return violations;
    }

}