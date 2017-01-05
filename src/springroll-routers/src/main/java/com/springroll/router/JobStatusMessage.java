package com.springroll.router;

import com.springroll.notification.AbstractNotificationMessage;
import com.springroll.orm.entities.Job;

import java.time.LocalDateTime;

/**
 * Created by anishjoseph on 05/01/17.
 */
public class JobStatusMessage extends AbstractNotificationMessage {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean completed;
    private Boolean underReview;
    private String status;
    private String service;
    private String userId;

    public JobStatusMessage(Job job) {
        this.id = job.getID();
        this.startTime = job.getStartTime();
        this.endTime = job.getEndTime();
        this.completed = job.isCompleted();
        this.underReview = job.isUnderReview();
        this.status = job.getStatus();
        this.service = job.getService();
        this.userId = job.getUserId();

    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Boolean getUnderReview() {
        return underReview;
    }

    public void setUnderReview(Boolean underReview) {
        this.underReview = underReview;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
