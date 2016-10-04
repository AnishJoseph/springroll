package com.springroll.router.review;


import com.springroll.notification.AbstractNotificationPayload;

/**
 * Created by anishjoseph on 02/10/16.
 */
public class ReviewNotificationPayload extends AbstractNotificationPayload {
    private Long reviewStepId;

    public ReviewNotificationPayload(){}

    public ReviewNotificationPayload(Long reviewStepId) {
        this.reviewStepId = reviewStepId;
    }

    public Long getReviewStepId() {
        return reviewStepId;
    }

    public void setReviewStepId(Long reviewStepId) {
        this.reviewStepId = reviewStepId;
    }
}
