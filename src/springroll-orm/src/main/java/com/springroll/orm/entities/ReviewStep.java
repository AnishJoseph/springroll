package com.springroll.orm.entities;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.springroll.core.AckLog;
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

    @Column(name = "REVIEW_STAGE")
    @Min(1)
    @Max(100)
    private int reviewStage;

    @Column(name = "NOTIFICATION_ID")
    private Long notificationId;

    @Column(name = "COMPLETED")
    private boolean completed = false;

    @Column(name = "EVENT")
    @Lob
    private byte[] serializedEvent;

    @Column(name = "REVIEW_LOG")
    private String reviewLogAsJson = "";

    private transient List<ReviewLog> reviewLog;
    private transient IEvent event;

    public ReviewStep(){}
    public ReviewStep(Long ruleId, int reviewStage, Long parentId) {
        this.ruleId = ruleId;
        this.reviewStage = reviewStage;
        this.setParentId(parentId);
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
}