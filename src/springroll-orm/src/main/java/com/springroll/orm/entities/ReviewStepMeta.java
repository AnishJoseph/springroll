package com.springroll.orm.entities;


import com.springroll.core.IEvent;
import com.springroll.core.ReviewLog;
import com.springroll.orm.JavaSerializationConverter;
import com.springroll.orm.ReviewLogConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
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

    public ReviewStepMeta(){}

    public IEvent getEvent() {
        return event;
    }

    public void setEvent(IEvent event) {
        this.event = event;
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

}