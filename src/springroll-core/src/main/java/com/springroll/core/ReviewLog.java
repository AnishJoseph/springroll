package com.springroll.core;

import java.time.LocalDateTime;

/**
 * Created by anishjoseph on 29/09/16.
 *
 */
public class ReviewLog extends AckLog {
    private boolean approved;
    private String reviewComment;

    public ReviewLog(){}
    public ReviewLog(String reviewer, LocalDateTime time, boolean approved, String reviewComment) {
        super(reviewer, time);
        this.approved = approved;
        this.reviewComment = reviewComment;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }
}
