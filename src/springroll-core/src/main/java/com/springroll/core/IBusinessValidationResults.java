package com.springroll.core;

/**
 * Created by anishjoseph on 27/09/16.
 */
public interface IBusinessValidationResults {
    void addOverrideableViolation(String field, String messageKey, String[] args);
    void addBusinessViolation(String field, String messageKey, String[] args);
    void addWarningToGroup(String field, String messageKey, String[] args);
    void addReviewNeeded(String field, String messageKey, String[] args, String violatedRule);
    void addReviewNeeded(String field, String messageKey, String[] args, String violatedRule, String userOrGroup);
    void addReviewNeeded(String violatedRule, String userOrGroup);
    void addReviewNeeded(String violatedRule);
}
