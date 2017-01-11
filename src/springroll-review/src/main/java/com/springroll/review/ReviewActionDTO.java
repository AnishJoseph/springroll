package com.springroll.review;

import com.springroll.core.ServiceDefinition;
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
    private String serviceInstance;

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
    public ServiceDefinition getServiceDefinition() {
        return CoreServiceDefinitions.REVIEW;
    }

    @Override
    public String getServiceInstance() {
        return serviceInstance;
    }

    public void setServiceInstance(String serviceInstance) {
        this.serviceInstance = serviceInstance;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }
}
