package com.springroll.orm.entities;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.springroll.core.DTO;
import com.springroll.core.ReviewData;
import org.hibernate.annotations.Type;
import org.hibernate.internal.util.SerializationHelper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by anishjoseph on 05/09/16.
 */

@Entity
public class Job extends AbstractEntity {

    private transient List<? extends DTO> payloads;
    private transient List<ReviewData> reviewData;

    public Job(){
        this.setStartTime(LocalDateTime.now());
    }
    @Column(name = "SERIALIZED_PAYLOADS")
    @Lob
    private byte[] serializedPayloads;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "Start_Time")
    @Type(type="com.springroll.orm.LocalDateTimeUserType")
    private LocalDateTime startTime;

    @Column(name = "End_Time")
    @Type(type="com.springroll.orm.LocalDateTimeUserType")
    private LocalDateTime endTime;

    @Column(name = "COMPLETED")
    private boolean completed;

    @Column(name = "UNDER_REVIEW")
    private boolean underReview;

    private String status;

    private String service;

    @Column(name = "SERIALIZED_REVIEW_DATA")
    private String serializedReviewData;


    public Job(Long parentId, boolean underReview, String service, String userId, List<? extends DTO> payloads) {
        this.setParentId(parentId);
        this.underReview = underReview;
        this.service = service;
        this.userId = userId;
        this.setPayloads(payloads);
        this.setStartTime(LocalDateTime.now());
    }


    public boolean isUnderReview() {
        return underReview;
    }

    public void setUnderReview(boolean underReview) {
        this.underReview = underReview;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
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
        if (this.payloads == null)
            this.payloads = (List<? extends DTO>) SerializationHelper.deserialize(serializedPayloads);
        return payloads;
    }

    public void setPayloads(List<? extends DTO> payloads) {
        this.payloads = payloads;
        serializedPayloads = SerializationHelper.serialize((Serializable) payloads);
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

}