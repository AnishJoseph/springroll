package com.springroll.router.review;

import com.springroll.core.BusinessValidationResult;
import com.springroll.core.DTO;
import com.springroll.core.IDTOProcessors;
import com.springroll.core.IEvent;
import com.springroll.router.CoreDTOProcessors;

import java.util.List;

/**
 * Created by anishjoseph on 28/09/16.
 */
public class ReviewNeededDTO implements DTO {
    private IEvent eventForReview;
    private List<BusinessValidationResult> reviewNeededViolations;

    public ReviewNeededDTO(IEvent eventForReview, List<BusinessValidationResult> reviewNeededViolations) {
        this.eventForReview = eventForReview;
        this.reviewNeededViolations = reviewNeededViolations;
    }

    public IEvent getEventForReview() {
        return eventForReview;
    }

    public void setEventForReview(IEvent eventForReview) {
        this.eventForReview = eventForReview;
    }

    public List<BusinessValidationResult> getReviewNeededViolations() {
        return reviewNeededViolations;
    }

    public void setReviewNeededViolations(List<BusinessValidationResult> reviewNeededViolations) {
        this.reviewNeededViolations = reviewNeededViolations;
    }
    @Override
    public IDTOProcessors getProcessor() {
        return CoreDTOProcessors.NULL;
    }

}
