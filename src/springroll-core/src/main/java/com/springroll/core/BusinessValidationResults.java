package com.springroll.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anishjoseph on 27/09/16.
 */
public class BusinessValidationResults implements IBusinessValidationResults, Serializable{
    private List<BusinessValidationResult> businessViolations = new ArrayList<BusinessValidationResult>();
    private List<BusinessValidationResult> reviewNeededViolations = new ArrayList<>();


    @Override
    public void addBusinessViolation(int dtoIndex, String field, String messageKey, String[] args) {
        businessViolations.add(new BusinessValidationResult(dtoIndex, field, messageKey, args, null, null));
    }

    @Override
    public void addReviewNeeded(int dtoIndex, String field, String messageKey, String[] args, String violatedRule) {
        reviewNeededViolations.add(new BusinessValidationResult(dtoIndex, field, messageKey, args, violatedRule, null));
    }

    @Override
    public void addReviewNeeded(int dtoIndex, String field, String messageKey, String[] args, String violatedRule, String approver) {
        reviewNeededViolations.add(new BusinessValidationResult(dtoIndex, field, messageKey, args, violatedRule, approver));
    }

    public List<BusinessValidationResult> getBusinessViolations() {
        return businessViolations;
    }

    public List<BusinessValidationResult> getReviewNeededViolations() {
        return reviewNeededViolations;
    }

}
