package com.springroll.router.review;


import com.springroll.core.*;

import java.util.List;

/**
 * Created by anishjoseph on 11/09/16.
 */
public class ReviewNeededEvent extends AbstractEvent<ReviewNeededDTO> implements ISignallingEvent {
    public ReviewNeededEvent(IEvent event, List<BusinessValidationResult> reviewNeededViolations, SpringrollUser user) {
        ReviewNeededDTO reviewNeededDTO = new ReviewNeededDTO(event, reviewNeededViolations);
        this.setUser(user);
        this.setPayload(reviewNeededDTO);
    }
}
