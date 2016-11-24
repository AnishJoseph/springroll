package com.springroll.core;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by anishjoseph on 29/09/16.
 *
 */
public class AckLog implements Serializable{
    private String reviewer;
    private LocalDateTime time;

    public AckLog(){}
    public AckLog(String reviewer, LocalDateTime time) {
        this.reviewer = reviewer;
        this.time = time;
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

}
