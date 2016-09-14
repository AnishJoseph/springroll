package com.springroll.router;


import com.springroll.core.DTO;
import com.springroll.core.Principal;

import java.util.List;

/**
 * Created by anishjoseph on 14/09/16.
 */
public class JobMeta {
    private List<? extends DTO> payloads;
    private Principal userContext;
    private Long jobId;
    private Long parentJobId;
    boolean needsBusinessValidation = true;
    private boolean isAsynchronous = true;

    public JobMeta(List<? extends DTO> payloads, Principal userContext, Long jobId, Long parentJobId, boolean needsBusinessValidation, boolean isAsynchronous) {
        this.payloads = payloads;
        this.userContext = userContext;
        this.jobId = jobId;
        this.parentJobId = parentJobId;
        this.needsBusinessValidation = needsBusinessValidation;
        this.isAsynchronous = isAsynchronous;
    }

    public List<? extends DTO> getPayloads() {
        return payloads;
    }

    public void setPayloads(List<DTO> payloads) {
        this.payloads = payloads;
    }

    public Principal getUserContext() {
        return userContext;
    }

    public void setUserContext(Principal userContext) {
        this.userContext = userContext;
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

    public boolean isNeedsBusinessValidation() {
        return needsBusinessValidation;
    }

    public void setNeedsBusinessValidation(boolean needsBusinessValidation) {
        this.needsBusinessValidation = needsBusinessValidation;
    }

    public boolean isAsynchronous() {
        return isAsynchronous;
    }

    public void setAsynchronous(boolean asynchronous) {
        isAsynchronous = asynchronous;
    }
}
