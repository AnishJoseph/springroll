package com.springroll.router;

import com.springroll.orm.entities.Job;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anishjoseph on 05/01/17.
 */
public class JobStatusMessage {

    List<Object> updatedData = new ArrayList<>();

    public JobStatusMessage(Job job) {
        /* This should match the named query in sr.named.queries.xml and the grid defn in the json file */
        updatedData.add(job.getID());
        updatedData.add(job.getService());
        updatedData.add(job.getServiceDescription());
        updatedData.add(job.getStartTime());
        updatedData.add(job.getEndTime());
        updatedData.add(job.getJobStatus());
        updatedData.add(job.getPendingReviewers());
        updatedData.add(job.getReviewLog());
    }

    public List<Object> getUpdatedData() {
        return updatedData;
    }

    public void setUpdatedData(List<Object> updatedData) {
        this.updatedData = updatedData;
    }
}
