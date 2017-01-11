package com.springroll.core.services.review;

/**
 * Created by anishjoseph on 10/01/17.
 */
public interface ReviewService {
    String getServiceInstanceForReviewId(boolean approved, Long reviewStepId);
}
