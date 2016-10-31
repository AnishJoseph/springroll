package com.springroll.core;


/**
 * Holds the context for an Event - the jobId and legId in whose context the event was created and the Principal
 * @author Anish
 * @since 1.0
 */
public class ContextAttributes {
    private Long jobId = null;
    private Long legId = null;
    private SpringrollUser user;

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

    public SpringrollUser getUser() {
        return user;
    }

    public void setUser(SpringrollUser user) {
        this.user = user;
    }
}
