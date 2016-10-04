package com.springroll.core;

import java.time.LocalDateTime;

/**
 * Created by anishjoseph on 29/09/16.
 *
 */
public class ReviewLog extends AckLog {
    private boolean approved;

    public ReviewLog(){}
    public ReviewLog(String reviewer, LocalDateTime time, boolean approved) {
        super(reviewer, time);
        this.approved = approved;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
