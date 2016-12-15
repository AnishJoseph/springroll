package com.springroll.orm.entities;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.springroll.core.IEvent;
import com.springroll.core.ReviewLog;
import com.springroll.orm.JavaSerializationConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anishjoseph on 05/09/16.
 */

@Entity
@Table(name = "REVIEW_STEP_META")
public class ReviewStepMeta extends AbstractEntity {

    @Column(name = "INITIATOR")
    private String initiator;

    @Column(name = "SEARCH_ID")
    private String searchId;

    @Column(name = "EVENT")
    @Convert(converter = JavaSerializationConverter.class)
    private IEvent event;

    @Column(name = "REVIEW_LOG")
    private String reviewLogAsJson = "";

    private transient List<ReviewLog> reviewLog;

    public ReviewStepMeta(){}

    public IEvent getEvent() {
        return event;
    }

    public void setEvent(IEvent event) {
        this.event = event;
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

    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public void setReviewLog(List<ReviewLog> reviewLog) {
        this.reviewLog = reviewLog;
    }
}