package com.springroll.core;


import java.io.Serializable;
import java.util.List;

/**
 * Created by Anish Joseph on 10/09/16.
 * @since 1.0
 */
public interface IEvent<T extends DTO> extends Serializable {

    T getPayload();
    void setPayload(T payload);
    List<T> getPayloads();
    void setPayloads(List<T> payloads);
    boolean isRouted();
    void setRouted(boolean isNewTransaction);
    String getDestinationUri();
    void setDestinationUri(String destinationBean);
    Long getJobId();
    void setJobId(Long jobId);
    Long getLegId();
    void setLegId(Long legID);
    SpringrollUser getUser();
    void setUser(SpringrollUser user);
}
