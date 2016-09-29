package com.springroll.core;

import java.util.List;

/**
 * Created by anishjoseph on 29/09/16.
 */
public interface ReviewableEvent {
    void setReviewData(List<ReviewData> reviewData);
    void setApproved(boolean approved);

}
