package com.springrollexample.router.test;

import com.springroll.core.ReviewData;
import com.springroll.core.ReviewableEvent;

import java.util.List;

/**
 * Created by anishjoseph on 11/09/16.
 */
public class TestRootEvent extends AbstractTestEvent<TestDTO> implements ReviewableEvent{
    private static final long serialVersionUID = -1;
    private List<ReviewData> reviewData;
    private boolean approved;

    public List<ReviewData> getReviewData() {
        return reviewData;
    }

    public boolean isApproved() {
        return approved;
    }

    @Override
    public void setReviewData(List<ReviewData> reviewData) {
        this.reviewData = reviewData;
    }

    @Override
    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
