package com.springroll.notification;

import com.springroll.core.BusinessValidationResult;

/**
 * Created by anishjoseph on 02/10/16.
 */
public class FyiReviewNotificationMessage extends AbstractNotificationMessage{
    private BusinessValidationResult businessValidationResult;

    public FyiReviewNotificationMessage(){}

    public FyiReviewNotificationMessage(BusinessValidationResult businessValidationResult, String approver) {
        this.businessValidationResult = businessValidationResult;
        setNotificationReceivers(approver);
    }

    public BusinessValidationResult getBusinessValidationResult() {
        return businessValidationResult;
    }

    public void setBusinessValidationResult(BusinessValidationResult businessValidationResult) {
        this.businessValidationResult = businessValidationResult;
    }
}
