package com.springroll.notification;

import com.springroll.core.BusinessValidationResult;

import java.util.List;

/**
 * Created by anishjoseph on 02/10/16.
 */
public class MdmReviewNotificationMessage extends AbstractNotificationMessage{
    private List<Long> reviewStepId;
    private String messageKey;
    private String[] args = new String[]{};
    public MdmReviewNotificationMessage(){}

    public MdmReviewNotificationMessage(List<Long> reviewStepId, String approver, String messageKey, String[] args) {
        setNotificationReceivers(approver);
        this.reviewStepId = reviewStepId;
        this.messageKey = messageKey;
        this.args = args;
    }

    public List<Long> getReviewStepId() {
        return reviewStepId;
    }

    public void setReviewStepId(List<Long> reviewStepId) {
        this.reviewStepId = reviewStepId;
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
}
