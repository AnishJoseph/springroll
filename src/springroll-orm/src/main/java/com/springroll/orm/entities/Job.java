package com.springroll.orm.entities;


import com.springroll.core.DTO;
import com.springroll.core.JobStatus;
import com.springroll.core.ReviewLog;
import com.springroll.orm.JavaSerializationConverter;
import com.springroll.orm.ReviewLogConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anishjoseph on 05/09/16.
 */

@Entity
public class Job extends AbstractEntity {

    public Job(){
        this.setStartTime(LocalDateTime.now());
    }

    @Column(name = "PAYLOADS")
    @Convert(converter = JavaSerializationConverter.class)
    private List<? extends DTO> payloads;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "Start_Time")
    private LocalDateTime startTime;

    @Column(name = "End_Time")
    private LocalDateTime endTime;

    @Column(name = "COMPLETED")
    private Boolean completed;

    @Column(name = "JOB_STATUS")
    private JobStatus jobStatus = JobStatus.InProgress;

    private String status;

    @Column(name = "SERVICE")
    private String service;

    @Column(name = "SERVICE_DESCRIPTION")
    private String serviceDescription;

    @Column(name = "PENDING_REVIEWERS")
    private String pendingReviewers;

    @Column(name = "REVIEW_LOG")
    @Convert(converter = ReviewLogConverter.class)
    private List<ReviewLog> reviewLog = new ArrayList<>();


    public Job(Long parentId, Boolean underReview, String service, String userId, List<? extends DTO> payloads, String serviceDescription) {
        this.setParentId(parentId);
        if(underReview)this.jobStatus = JobStatus.UnderReview;
        this.service = service;
        this.userId = userId;
        this.setPayloads(payloads);
        this.setStartTime(LocalDateTime.now());
        this.serviceDescription = serviceDescription;
    }


    public Boolean isCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public List<? extends DTO> getPayloads() {
        return payloads;
    }

    public void setPayloads(List<? extends DTO> payloads) {
        this.payloads = payloads;
    }

    public List<ReviewLog> getReviewLog() {
        return reviewLog;
    }

    public void setReviewLog(List<ReviewLog> reviewLog) {
        this.reviewLog = reviewLog;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public void setServiceDescription(String serviceInstance) {
        this.serviceDescription = serviceInstance;
    }

    public JobStatus getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(JobStatus jobStatus) {
        this.jobStatus = jobStatus;
    }

    public String getPendingReviewers() {
        return pendingReviewers;
    }

    public void setPendingReviewers(String pendingReviewers) {
        this.pendingReviewers = pendingReviewers;
    }
    public void addReviewLog(ReviewLog reviewLog){
        this.reviewLog.add(reviewLog);
    }
}