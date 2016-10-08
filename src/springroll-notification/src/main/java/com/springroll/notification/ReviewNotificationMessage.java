package com.springroll.notification;

import java.util.List;

/**
 * Created by anishjoseph on 02/10/16.
 */
public class ReviewNotificationMessage extends AbstractNotificationMessage{
    private List<Long> reviewStepId;

    public ReviewNotificationMessage(){}

    public ReviewNotificationMessage(List<Long> reviewStepId, String approver) {
        this.reviewStepId = reviewStepId;
        setNotificationReceivers(approver);
    }

    public List<Long> getReviewStepId() {
        return reviewStepId;
    }

    public void setReviewStepId(List<Long> reviewStepId) {
        this.reviewStepId = reviewStepId;
    }
}
