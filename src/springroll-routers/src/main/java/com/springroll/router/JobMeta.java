package com.springroll.router;


import com.springroll.core.BusinessValidationResults;
import com.springroll.core.ServiceDTO;

import java.util.List;

/**
 * Created by anishjoseph on 14/09/16.
 */
public class JobMeta {
    private List<? extends ServiceDTO> payloads;
    private Long parentJobId;
    private boolean isAsynchronous = true;
    private BusinessValidationResults businessValidationResults;



    public JobMeta(List<? extends ServiceDTO> payloads, Long parentJobId, boolean businessValidationAlreadyDoneOnce, boolean isAsynchronous) {
        this.payloads = payloads;
        this.parentJobId = parentJobId;
        this.isAsynchronous = isAsynchronous;
    }

    public List<? extends ServiceDTO> getPayloads() {
        return payloads;
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

    public void setPayloads(List<? extends ServiceDTO> payloads) {
        this.payloads = payloads;
    }

    public BusinessValidationResults getBusinessValidationResults() {
        return businessValidationResults;
    }

    public void setBusinessValidationResults(BusinessValidationResults businessValidationResults) {
        this.businessValidationResults = businessValidationResults;
    }
}
