package com.springroll.review;

import com.springroll.core.BusinessValidationResult;
import com.springroll.core.ReviewLog;
import com.springroll.core.notification.IReviewMeta;
import com.springroll.notification.AbstractNotificationMessage;

import java.util.List;

/**
 * Created by anishjoseph on 02/10/16.
 */
public class FyiReviewNotificationMessage extends AbstractNotificationMessage {
    private List<BusinessValidationResult> businessValidationResult;
    private String messageKey;
    private String[] args = new String[]{};
    private List<ReviewLog> reviewLog;


    public FyiReviewNotificationMessage(){}

    public FyiReviewNotificationMessage(IReviewMeta reviewMeta, String messageKey, String[] args) {
        setNotificationReceivers(reviewMeta.getApprover());
        this.businessValidationResult = reviewMeta.getBusinessValidationResults();
        this.messageKey = messageKey;
        if(args != null)this.args = args;
        this.reviewLog = reviewMeta.getReviewLogs();
        setInitiator(reviewMeta.getInitiator());
    }

    public List<BusinessValidationResult> getBusinessValidationResult() {
        return businessValidationResult;
    }

    public void setBusinessValidationResult(List<BusinessValidationResult> businessValidationResult) {
        this.businessValidationResult = businessValidationResult;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public List<ReviewLog> getReviewLog() {
        return reviewLog;
    }

    public void setReviewLog(List<ReviewLog> reviewLog) {
        this.reviewLog = reviewLog;
    }
}
