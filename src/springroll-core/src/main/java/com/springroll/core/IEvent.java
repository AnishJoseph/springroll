package com.springroll.core;

import java.io.Serializable;
import java.util.List;

/**
 * Created by anishjoseph on 10/09/16.
 */
public interface IEvent<T extends DTO> extends Serializable {

    T getPayload();
    void setPayload(T payload);
    List<T> getPayloads();
    void setPayloads(List<T> payloads);
    boolean isRouted();
    void setRouted(boolean isNewTransaction);
    String getDestinationBean();
    void setDestinationBean(String destinationBean);
    Long getJobId();
    void setJobId(Long jobId);
    Long getLegId();
    void setLegId(Long legID);
    boolean isRestartedDueToDeadLock();
    void setRestartedDueToDeadLock(boolean restartedDueToDeadLock);
    boolean isFirstTransactionLeg();
    void setFirstTransactionLeg(boolean firstTransactionLeg);
    int getMsgId();
    void setMsgId(int hexMsgId);
    Principal getPrincipal();
    void setPrincipal(Principal principal);
}
