package com.springroll.router.review;

import com.springroll.core.DTO;
import com.springroll.core.IDTOProcessors;
import com.springroll.router.CoreDTOProcessors;

/**
 * Created by anishjoseph on 28/09/16.
 */
public class ReviewActionDTO implements DTO{
    private Long reviewStepId;
    private boolean approved;

    public ReviewActionDTO(){}

    public ReviewActionDTO(Long reviewStepId, boolean approved) {
        this.reviewStepId = reviewStepId;
        this.approved = approved;
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

    @Override
    public IDTOProcessors getProcessor() {
        return CoreDTOProcessors.REVIEW;
    }
}
