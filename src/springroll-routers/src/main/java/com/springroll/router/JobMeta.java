package com.springroll.router;


import com.springroll.core.BusinessValidationResults;
import com.springroll.core.ServiceDTO;
import com.springroll.core.SpringrollUser;

import java.util.List;

/**
 * Created by anishjoseph on 14/09/16.
 */
public class JobMeta {
    private List<? extends ServiceDTO> payloads;
    private SpringrollUser user;
    private Long jobId;
    private Long legId;
    private Long parentJobId;
    private boolean isAsynchronous = true;
    private BusinessValidationResults businessValidationResults;



    public JobMeta(List<? extends ServiceDTO> payloads, SpringrollUser user, Long jobId, Long legId, Long parentJobId, boolean businessValidationAlreadyDoneOnce, boolean isAsynchronous) {
        this.payloads = payloads;
        this.user = user;
        this.jobId = jobId;
        this.parentJobId = parentJobId;
        this.isAsynchronous = isAsynchronous;
        this.legId = legId;
    }

    public List<? extends ServiceDTO> getPayloads() {
        return payloads;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getParentJobId() {
        return parentJobId;
    }

    public void setParentJobId(Long parentJobId) {
        this.parentJobId = parentJobId;
    }

    public boolean isAsynchronous() {
        return isAsynchronous;
    }

    public void setAsynchronous(boolean asynchronous) {
        isAsynchronous = asynchronous;
    }

    public SpringrollUser getUser() {
        return user;
    }

    public void setUser(SpringrollUser user) {
        this.user = user;
    }

    public void setPayloads(List<? extends ServiceDTO> payloads) {
        this.payloads = payloads;
    }

    public Long getLegId() {
        return legId;
    }

    public void setLegId(Long legId) {
        this.legId = legId;
    }

    public BusinessValidationResults getBusinessValidationResults() {
        return businessValidationResults;
    }

    public void setBusinessValidationResults(BusinessValidationResults businessValidationResults) {
        this.businessValidationResults = businessValidationResults;
    }
}
