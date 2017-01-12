package com.springroll.review;

import com.springroll.core.services.notification.IReviewMeta;
import com.springroll.core.services.notification.AlertType;

import java.util.List;

/**
 * Created by anishjoseph on 02/10/16.
 */
public class ReviewNotificationMessage extends AbstractReviewNotificationMessage{
    private List<Long> reviewStepId;
    {
        alertType = AlertType.ACTION;
    }

    public ReviewNotificationMessage(){
    }

    public ReviewNotificationMessage(IReviewMeta reviewMeta, String messageKey, String[] args) {
        super(reviewMeta, messageKey, args);
        this.reviewStepId = reviewMeta.getReviewStepIds();
    }

    public List<Long> getReviewStepId() {
        return reviewStepId;
    }

    public void setReviewStepId(List<Long> reviewStepId) {
        this.reviewStepId = reviewStepId;
    }
}
