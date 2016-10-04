package com.springrollexample.router.test;

import com.springroll.core.ReviewLog;
import com.springroll.core.ReviewableEvent;

import java.util.List;

/**
 * Created by anishjoseph on 11/09/16.
 */
public class TestRootEvent extends AbstractTestEvent<TestDTO> implements ReviewableEvent{
    private static final long serialVersionUID = -1;
    private List<ReviewLog> reviewLog;
    private boolean approved;

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
