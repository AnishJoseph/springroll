package com.springroll.orm.entities;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.springroll.core.BusinessValidationResult;
import com.springroll.core.ReviewLog;
import com.springroll.orm.JavaSerializationConverter;
import com.springroll.orm.ReviewLogConverter;

import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anishjoseph on 05/09/16.
 */

@Entity
@Table(name = "REVIEW_STEP")
@NamedQueries({
        @NamedQuery(name="ReviewStep.findByParentIds", query = "SELECT o FROM ReviewStep o where o.parentId  in :parentIds"),
})
public class ReviewStep extends AbstractEntity {

    @Column(name = "RULE_ID")
    private Long ruleId;

    @Column(name = "CHANNEL")
    private String channel;

    @Column(name = "REVIEW_STAGE")
    private int reviewStage;

    @Column(name = "APPROVER")
    private String approver;

    @Column(name = "NOTIFICATION_ID")
    private Long notificationId;

    @Column(name = "COMPLETED")
    private Boolean completed = false;

    @Column(name = "REVIEW_LOG")
    @Convert(converter = ReviewLogConverter.class)
    private List<ReviewLog> reviewLog = new ArrayList<>();

    @Column(name = "VIOLATION_FOR_THIS_STEP", columnDefinition = "BLOB")
    @Convert(converter = JavaSerializationConverter.class)
    private BusinessValidationResult violationForThisStep;

    public BusinessValidationResult getViolationForThisStep() {
        return violationForThisStep;
    }

    public void setViolationForThisStep(BusinessValidationResult violationForThisStep) {
        this.violationForThisStep = violationForThisStep;
    }

    public ReviewStep() {
    }

    public ReviewStep(Long ruleId, String channel, int reviewStage, Long parentId, String approver, BusinessValidationResult violationForThisStep) {
        this.ruleId = ruleId;
        this.reviewStage = reviewStage;
        this.setParentId(parentId);
        this.approver = approver;
        this.channel = channel;
        this.setViolationForThisStep(violationForThisStep);
    }


    public void addReviewLog(ReviewLog reviewLog){
        this.reviewLog.add(reviewLog);
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public Boolean isCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public int getReviewStage() {
        return reviewStage;
    }

    public void setReviewStage(int reviewStage) {
        this.reviewStage = reviewStage;
    }


    public Boolean hasThisUserAlreadyReviewedThisStep(String userId) {
        for (ReviewLog data : reviewLog) {
            if (data.getReviewer().equals(userId)) return true;
        }
        return false;

    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public List<ReviewLog> getReviewLog() {
        return reviewLog;
    }

    public void setReviewLog(List<ReviewLog> reviewLog) {
        this.reviewLog = reviewLog;
    }
}