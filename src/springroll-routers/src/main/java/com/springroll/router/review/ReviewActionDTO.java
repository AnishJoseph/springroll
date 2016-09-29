package com.springroll.router.review;

import com.springroll.core.DTO;

/**
 * Created by anishjoseph on 28/09/16.
 */
public class ReviewActionDTO implements DTO{
    private Long notificationId;
    private Long reviewStepId;
    private boolean approved;

    public ReviewActionDTO(Long notificationId, Long reviewStepId, boolean approved) {
        this.notificationId = notificationId;
        this.reviewStepId = reviewStepId;
        this.approved = approved;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public Long getReviewStepId() {
        return reviewStepId;
    }

    public void setReviewStepId(Long reviewStepId) {
        this.reviewStepId = reviewStepId;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
