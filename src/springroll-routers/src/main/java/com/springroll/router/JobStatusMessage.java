package com.springroll.router;

import com.springroll.notification.AbstractNotificationMessage;
import com.springroll.orm.entities.Job;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anishjoseph on 05/01/17.
 */
public class JobStatusMessage extends AbstractNotificationMessage {

    List<Object> updatedData = new ArrayList<>();

    private String userId;

    public JobStatusMessage(Job job) {
        /* This should match the named query in sr.named.queries.xml and the grid defn in the json file */
        updatedData.add(job.getID());
        updatedData.add(job.getService());
        updatedData.add(job.getServiceInstance());
        updatedData.add(job.getStartTime());
        updatedData.add(job.getEndTime());
        updatedData.add(job.isCompleted());
        updatedData.add(job.isUnderReview());
        updatedData.add(job.getStatus());
        this.userId = job.getUserId();

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Object> getUpdatedData() {
        return updatedData;
    }

    public void setUpdatedData(List<Object> updatedData) {
        this.updatedData = updatedData;
    }
}
