package com.springroll.core;

import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anishjoseph
 * <p>
 * Created by anishjoseph on 10/09/16.
 *    </p>
 * anish ';kj;as '';lj qwqwkp
 * @since 1.0
 */
public class AbstractEvent<T extends DTO> implements IEvent<T> {
    private static final long serialVersionUID = 1L;

    private User user;
    private List<T> payloads;
    private boolean routed = false;
    private String destinationUri;
    private Long jobId = new Long(0);
    private Long legId = new Long(0);

    @Override public T getPayload() {
        if(payloads != null && payloads.size() > 0){
            return payloads.get(0);
        }
		return null;
	}

    @Override public void setPayload(T payload) {
        payloads = new ArrayList<T>(1);
        payloads.add(payload);
	}

    @Override
    public List<T> getPayloads() {
        return payloads;
    }

    @Override
    public void setPayloads(List<T> payloads) {
        this.payloads = payloads;
    }

    @Override
    public boolean isRouted() {
        return routed;
    }

    @Override
    public void setRouted(boolean isNewTransaction) {
        this.routed = isNewTransaction;
    }

    @Override
    public String getDestinationUri() {
        return destinationUri;
    }

    @Override
    public void setDestinationUri(String destinationBean) {
        this.destinationUri = destinationBean;
    }

    @Override
    public Long getJobId()
    {
        return jobId;
    }

    @Override
    public void setJobId(Long jobId)
    {
        this.jobId = jobId;
    }

    @Override
    public Long getLegId() {
        return legId;
    }

    @Override
    public void setLegId(Long legId) {
        this.legId = legId;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

}
