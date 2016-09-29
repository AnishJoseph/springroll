package com.springroll.orm.entities;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.springroll.core.IEvent;
import com.springroll.core.ReviewData;
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

    @Column(name = "COMPLETED")
    private boolean completed = false;

    @Column(name = "SERIALIZED_EVENT")
    @Lob
    private byte[] serializedEvent;

    @Column(name = "SERIALIZED_REVIEW_DATA")
    private String serializedReviewData;

    private transient List<ReviewData> reviewData;
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

    public List<ReviewData> getReviewData() {
        if (this.reviewData == null && serializedReviewData != null) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            try {
                this.reviewData = mapper.readValue(serializedReviewData, new TypeReference<List<ReviewData>>(){});
            } catch (IOException e) {
                return null;
            }
        }
        return reviewData;
    }

    public void setReviewData(List<ReviewData> reviewData) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        this.reviewData = reviewData;
        try {
            serializedReviewData = mapper.writeValueAsString(reviewData);
        } catch (JsonProcessingException e) {
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

    public void addReviewData(ReviewData reviewData){
        getReviewData();
        if(this.reviewData == null) this.reviewData = new ArrayList<>();
        this.reviewData.add(reviewData);
        this.setReviewData(this.reviewData);
    }
}