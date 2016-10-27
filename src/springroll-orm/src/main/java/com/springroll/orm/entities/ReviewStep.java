package com.springroll.orm.entities;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.springroll.core.BusinessValidationResult;
import com.springroll.core.IEvent;
import com.springroll.core.ReviewLog;
import org.hibernate.internal.util.SerializationHelper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anishjoseph on 05/09/16.
 */

@Entity
@Table(name = "REVIEW_STEP")
public class ReviewStep extends AbstractEntity {

    @Column(name = "RULE_ID")
    private Long ruleId;

    @Column(name = "CHANNEL")
    private String channel;

    @Column(name = "REVIEW_STAGE")
    @Min(0)
    @Max(100)
    private int reviewStage;

    @Column(name = "APPROVER")
    private String approver;

    @Column(name = "NOTIFICATION_ID")
    private Long notificationId;

    @Column(name = "COMPLETED")
    private boolean completed = false;

    @Column(name = "EVENT")
    @Lob
    private byte[] serializedEvent;

    @Column(name = "REVIEW_LOG")
    private String reviewLogAsJson = "";

    @Column(name = "BUSINESS_VIOLATIONS")
    private String businessViolationsAsJson = "";

    @Column(name = "VIOLATION_FOR_THIS_STEP")
    private String violationForThisStepJson = "";

    private transient BusinessValidationResult violationForThisStep;


    public BusinessValidationResult getViolationForThisStep() {
        if (this.violationForThisStepJson.isEmpty() )return null;
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            violationForThisStep = mapper.readValue(violationForThisStepJson, BusinessValidationResult.class);
            return violationForThisStep;
        } catch (IOException e) {
            //FIXME
            throw new RuntimeException(e);
        }
    }

    public void setViolationForThisStep(BusinessValidationResult violationForThisStep) {
        this.violationForThisStep = violationForThisStep;
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            violationForThisStepJson = mapper.writeValueAsString(violationForThisStep);
        } catch (JsonProcessingException e) {
            //FIXME
            throw new RuntimeException(e);
        }
    }

    public List<BusinessValidationResult>  getBusinessViolations() {
        if (this.businessViolationsAsJson.isEmpty() )return new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            return mapper.readValue(businessViolationsAsJson, new TypeReference<List<BusinessValidationResult>>(){});
        } catch (IOException e) {
            //FIXME
            throw new RuntimeException(e);
        }
    }

    public void setBusinessViolations(List<BusinessValidationResult> reviewNeededViolations) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            businessViolationsAsJson = mapper.writeValueAsString(reviewNeededViolations);
        } catch (JsonProcessingException e) {
            //FIXME
            throw new RuntimeException(e);
        }
    }

    private transient List<ReviewLog> reviewLog;
    private transient IEvent event;

    public ReviewStep(){}
    public ReviewStep(Long ruleId, String channel, int reviewStage, Long parentId, String approver, BusinessValidationResult violationForThisStep) {
        this.ruleId = ruleId;
        this.reviewStage = reviewStage;
        this.setParentId(parentId);
        this.approver = approver;
        this.channel = channel;
        this.setViolationForThisStep(violationForThisStep);
    }

    public IEvent getEvent() {
        if (this.event == null)
            this.event = (IEvent) SerializationHelper.deserialize(serializedEvent);
        return event;
    }

    public void setEvent(IEvent event) {
        this.event = event;
        serializedEvent = SerializationHelper.serialize((Serializable) event);
    }

    public List<ReviewLog> getReviewLog() {
        if(reviewLogAsJson.isEmpty()){
            reviewLog = new ArrayList<>();
            return reviewLog;
        }

        if (this.reviewLog == null) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            try {
                this.reviewLog = mapper.readValue(reviewLogAsJson, new TypeReference<List<ReviewLog>>(){});
            } catch (IOException e) {
                //FIXME
                throw new RuntimeException(e);
            }
        }
        return reviewLog;
    }

    public void addReviewLog(ReviewLog reviewLog){
        getReviewLog();
        this.reviewLog.add(reviewLog);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            reviewLogAsJson = mapper.writeValueAsString(this.reviewLog);
        } catch (JsonProcessingException e) {
            //FIXME
            throw new RuntimeException(e);
        }
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getReviewStage() {
        return reviewStage;
    }

    public void setReviewStage(int reviewStage) {
        this.reviewStage = reviewStage;
    }


    public boolean hasThisUserAlreadyReviewedThisStep(String  userId){
        List<ReviewLog> reviewLog = getReviewLog();
        if(reviewLog == null)return false;
        for (ReviewLog data : reviewLog) {
            if(data.getReviewer().equals(userId))return true;
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
}