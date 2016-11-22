package com.springroll.review;

import com.springroll.core.IServiceDefinition;
import com.springroll.core.ServiceDTO;
import com.springroll.router.CoreServiceDefinitions;

import java.util.List;

/**
 * Created by anishjoseph on 28/09/16.
 */
public class ReviewActionDTO implements ServiceDTO {
    private List<Long> reviewStepId;
    private boolean approved;
    private String reviewComment;

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
    public IServiceDefinition getProcessor() {
        return CoreServiceDefinitions.REVIEW;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }
}
