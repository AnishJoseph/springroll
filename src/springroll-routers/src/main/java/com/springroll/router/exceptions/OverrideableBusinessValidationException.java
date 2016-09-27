package com.springroll.router.exceptions;

import com.springroll.core.BusinessValidationResult;

import java.util.List;

/**
 * Created by anishjoseph on 26/09/16.
 */
public class OverrideableBusinessValidationException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    private List<BusinessValidationResult>  violations;

    public OverrideableBusinessValidationException(List<BusinessValidationResult> violations) {
        super("Business Validation has asked for SELF OVERRIDE");
        this.violations = violations;
    }

    public List<BusinessValidationResult> getViolations() {
        return violations;
    }

}