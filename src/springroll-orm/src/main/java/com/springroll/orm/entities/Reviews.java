package com.springroll.orm.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by anishjoseph on 22/11/16.
 */
@Embeddable
public class Reviews{
    private boolean approved;
    private String comment;
    private String reviewer;
    private LocalDateTime time;

    public Reviews() {
    }

    public Reviews(boolean approved, String reviewer, String comment, LocalDateTime time) {
        this.approved = approved;
        this.reviewer = reviewer;
        this.comment = comment;
        this.time = time;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
