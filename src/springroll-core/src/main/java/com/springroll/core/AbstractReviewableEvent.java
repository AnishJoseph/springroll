package com.springroll.core;


import java.util.ArrayList;
import java.util.List;

/**
 * @author anishjoseph
 * <p>
 * Created by anishjoseph on 10/09/16.
 *    </p>
 * @since 1.0
 */
public abstract class AbstractReviewableEvent<T extends DTO> extends AbstractEvent<T> implements ReviewableEvent {
    private List<ReviewLog> reviewLog;
    private boolean approved = true;

    public List<ReviewLog> getReviewLog() {
        return reviewLog;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setReviewLog(List<ReviewLog> reviewLog) {
        this.reviewLog = reviewLog;
    }

    @Override
    public void setApproved(boolean approved) {
        this.approved = approved;
    }

}
