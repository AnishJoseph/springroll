package com.springroll.core.services;

/**
 * Created by anishjoseph on 30/09/16.
 */
public interface IReviewManager {
    /* return the Group that can review this step */
    String getApproverForReviewStep(Long reviewStepId);
}
