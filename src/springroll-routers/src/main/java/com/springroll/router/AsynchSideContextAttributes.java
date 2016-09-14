package com.springroll.router;

import com.springroll.core.Principal;

import java.math.BigDecimal;

/**
 * Created by anishjoseph on 14/09/16.
 */
public class AsynchSideContextAttributes {
    private Long jobId = null;
    private Long legId = null;
    private Principal principal;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getLegId() {
        return legId;
    }

    public void setLegId(Long legId) {
        this.legId = legId;
    }

    public Principal getPrincipal() {
        return principal;
    }

    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }
}
