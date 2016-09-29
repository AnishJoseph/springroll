package com.springroll.router.review;


import com.springroll.core.AbstractEvent;
import com.springroll.core.BusinessValidationResult;
import com.springroll.core.IEvent;
import com.springroll.core.ISignallingEvent;

import java.util.List;

/**
 * Created by anishjoseph on 11/09/16.
 */
public class ReviewNeededEvent extends AbstractEvent<ReviewNeededDTO> implements ISignallingEvent {
    public ReviewNeededEvent(IEvent event, List<BusinessValidationResult> reviewNeededViolations) {
        ReviewNeededDTO reviewNeededDTO = new ReviewNeededDTO(event, reviewNeededViolations);
        this.setPayload(reviewNeededDTO);
    }
}
