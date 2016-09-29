package com.springroll.core;

import java.time.LocalDateTime;

/**
 * Created by anishjoseph on 29/09/16.
 *
 */
public class ReviewData {
    private String reviewer;
    private LocalDateTime time;
    private boolean approved;

    public ReviewData(){}
    public ReviewData(String reviewer, LocalDateTime time, boolean approved) {
        this.reviewer = reviewer;
        this.time = time;
        this.approved = approved;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
