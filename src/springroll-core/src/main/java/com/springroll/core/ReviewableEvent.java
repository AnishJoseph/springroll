package com.springroll.core;

import java.util.List;

/**
 * Created by anishjoseph on 29/09/16.
 */
public interface ReviewableEvent {
    void setReviewLog(List<ReviewLog> reviewLog);
    void setApproved(boolean approved);

}
