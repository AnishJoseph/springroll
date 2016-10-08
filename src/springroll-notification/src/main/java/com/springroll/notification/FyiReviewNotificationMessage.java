package com.springroll.notification;

import com.springroll.core.BusinessValidationResult;

import java.util.List;

/**
 * Created by anishjoseph on 02/10/16.
 */
public class FyiReviewNotificationMessage extends AbstractNotificationMessage{
    private List<BusinessValidationResult> businessValidationResult;

    public FyiReviewNotificationMessage(){}

    public FyiReviewNotificationMessage(List<BusinessValidationResult> businessValidationResult, String approver) {
        this.businessValidationResult = businessValidationResult;
        setNotificationReceivers(approver);
    }

    public List<BusinessValidationResult> getBusinessValidationResult() {
        return businessValidationResult;
    }

    public void setBusinessValidationResult(List<BusinessValidationResult> businessValidationResult) {
        this.businessValidationResult = businessValidationResult;
    }
}
