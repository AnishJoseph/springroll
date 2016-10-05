package com.springroll.notification;

/**
 * Created by anishjoseph on 02/10/16.
 */
public class ReviewNotificationMessage extends AbstractNotificationMessage{
    private Long reviewStepId;

    public ReviewNotificationMessage(){}

    public ReviewNotificationMessage(Long reviewStepId, String approver) {
        this.reviewStepId = reviewStepId;
        setNotificationReceivers(approver);
    }

    public Long getReviewStepId() {
        return reviewStepId;
    }

    public void setReviewStepId(Long reviewStepId) {
        this.reviewStepId = reviewStepId;
    }
}
