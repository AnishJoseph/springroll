package com.springroll.router.review;

import com.springroll.core.DTO;
import com.springroll.core.IDTOProcessors;
import com.springroll.router.CoreDTOProcessors;

import java.util.List;

/**
 * Created by anishjoseph on 28/09/16.
 */
public class ReviewActionDTO implements DTO{
    private List<Long> reviewStepId;
    private boolean approved;

    public ReviewActionDTO(){}

    public List<Long> getReviewStepId() {
        return reviewStepId;
    }

    public void setReviewStepId(List<Long> reviewStepId) {
        this.reviewStepId = reviewStepId;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @Override
    public IDTOProcessors getProcessor() {
        return CoreDTOProcessors.REVIEW;
    }
}
