package com.springroll.orm.entities;


import com.springroll.core.DTO;
import org.hibernate.annotations.Type;
import org.hibernate.internal.util.SerializationHelper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by anishjoseph on 05/09/16.
 */

@Entity
public class Job extends AbstractEntity {

    private transient List<? extends DTO> payloads;


    public Job(){
        this.setStartTime(LocalDateTime.now());
    }
    @Column(name = "SERIALIZED_PAYLOADS")
    @Lob
    private byte[] serializedPayloads;

    @Column(name = "Start_Time")
    @Type(type="com.springroll.orm.LocalDateTimeUserType")
    private LocalDateTime startTime;

    @Column(name = "End_Time")
    @Type(type="com.springroll.orm.LocalDateTimeUserType")
    private LocalDateTime endTime;

    @Column(name = "JOB_DONE")
    private boolean jobDone;

    private String status;

    public boolean isJobDone() {
        return jobDone;
    }

    public void setJobDone(boolean jobDone) {
        this.jobDone = jobDone;
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

    public List<? extends DTO> getPayloads() {
        if (this.payloads == null)
            this.payloads = (List<? extends DTO>) SerializationHelper.deserialize(serializedPayloads);
        return payloads;
    }

    public void setPayloads(List<? extends DTO> payloads) {
        this.payloads = payloads;
        serializedPayloads = SerializationHelper.serialize((Serializable) payloads);
    }
}