package com.springroll.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anishjoseph on 27/09/16.
 */
public class BusinessValidationResults implements IBusinessValidationResults, Serializable{
    private List<BusinessValidationResult> businessViolations = new ArrayList<BusinessValidationResult>();
    private List<BusinessValidationResult> overrideableViolations = new ArrayList<>();
    private List<BusinessValidationResult> reviewNeededViolations = new ArrayList<>();
    private List<BusinessValidationResult> groupWarningViolations = new ArrayList<>();


    @Override
    public void addOverrideableViolation(String field, String messageKey, String[] args) {
        overrideableViolations.add(new BusinessValidationResult(field, messageKey, args));
    }

    @Override
    public void addBusinessViolation(String field, String messageKey, String[] args) {
        businessViolations.add(new BusinessValidationResult(field, messageKey, args));
    }

    @Override
    public void addWarningToGroup(String field, String messageKey, String[] args) {
        groupWarningViolations.add(new BusinessValidationResult(field, messageKey, args));
    }

    @Override
    public void addReviewNeeded(String field, String messageKey, String[] args, String violatedRule) {
        reviewNeededViolations.add(new BusinessValidationResult(field, messageKey, args, violatedRule));
    }

    @Override
    public void addReviewNeeded(String field, String messageKey, String[] args, String violatedRule, String userOrGroup) {
        reviewNeededViolations.add(new BusinessValidationResult(field, messageKey, args, violatedRule, userOrGroup));
    }
    @Override
    public void addReviewNeeded(String violatedRule) {
        reviewNeededViolations.add(new BusinessValidationResult(violatedRule));
    }

    @Override
    public void addReviewNeeded(String violatedRule, String userOrGroup) {
        reviewNeededViolations.add(new BusinessValidationResult(violatedRule, userOrGroup));
    }

    public List<BusinessValidationResult> getBusinessViolations() {
        return businessViolations;
    }

    public List<BusinessValidationResult> getOverrideableViolations() {
        return overrideableViolations;
    }

    public List<BusinessValidationResult> getReviewNeededViolations() {
        return reviewNeededViolations;
    }

    public List<BusinessValidationResult> getGroupWarningViolations() {
        return groupWarningViolations;
    }
}
