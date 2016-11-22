package com.springroll.mdm;

import com.springroll.core.AbstractEvent;
import com.springroll.core.ReviewLog;
import com.springroll.core.ReviewableEvent;

import java.util.List;

/**
 * Created by anishjoseph on 28/09/16.
 */
public class MdmEvent extends AbstractEvent<MdmDTO> implements ReviewableEvent {
    private List<ReviewLog> reviewLog;
    private boolean approved;

    public List<ReviewLog> getReviewLog() {
        return reviewLog;
    }

    @Override
    public void setReviewLog(List<ReviewLog> reviewLog) {
        this.reviewLog = reviewLog;
    }

    public boolean isApproved() {
        return approved;
    }

    @Override
    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
