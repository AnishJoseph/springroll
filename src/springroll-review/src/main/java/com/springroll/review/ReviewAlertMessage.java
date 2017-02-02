package com.springroll.review;

import com.springroll.core.services.notification.IReviewMeta;
import com.springroll.core.services.notification.AlertType;

import java.util.List;

/**
 * Created by anishjoseph on 02/10/16.
 */
public class ReviewAlertMessage extends AbstractReviewAlertMessage {
    private List<Long> reviewStepId;
    {
        alertType = AlertType.ACTION;
    }

    public ReviewAlertMessage(){
    }

    public ReviewAlertMessage(IReviewMeta reviewMeta, String messageKey, String[] args) {
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
