package com.springroll.router.exceptions;

import java.util.Map;

/**
 * Created by anishjoseph on 26/09/16.
 */
public class PropertyValidationException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    private Map<String,Map<String,String>>  violations;

    public PropertyValidationException(Map<String, Map<String,String>> violations) {
        super("Property Validation Failure");
        this.violations = violations;
    }

    public Map<String,Map<String,String>> getViolations() {
        return violations;
    }

}