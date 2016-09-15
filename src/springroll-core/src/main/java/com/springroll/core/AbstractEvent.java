package com.springroll.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anishjoseph on 10/09/16.
 */
public class AbstractEvent<T extends DTO> implements IEvent<T> {
	private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(AbstractEvent.class);

    private Principal principal;
    private List<T>  _payloads;
    protected boolean translatable =false;
    private boolean translated =false;
    private boolean routed = false;
    private String destinationBean;
    private Long jobId = new Long(0);
    private Long legId = new Long(0);
    private boolean restartedDueToDeadLock = false;
    private boolean firstTransactionLeg = false;
    private int msgId = 0;

	public T getPayload() {
        if(_payloads != null && _payloads.size() > 0)
        {
            if(_payloads.size() > 1)
            {
               logger.warn("Payload size is greater than one - only the first will be returned.");
            }
            return _payloads.get(0);
        }

		return null;
	}

	public void setPayload(T payload) {
//        if(payload == null)
        _payloads = (List<T>) new ArrayList<T>(1);
        _payloads.add(payload);
	}

    public List<T> getPayloads() {
        return _payloads;
    }

    public void setPayloads(List<T> payloads) {
        _payloads = payloads;
    }

    public boolean isRouted() {
        return routed;
    }

    public void setRouted(boolean isNewTransaction) {
        this.routed = isNewTransaction;
    }


    public String getDestinationBean() {
        return destinationBean;
    }

    public void setDestinationBean(String destinationBean) {
        this.destinationBean = destinationBean;
    }


    public final void setTranslated(boolean translated) {
        this.translated = translated;
    }

    public final boolean isTranslationNeededAndIsNotReRouted() {
        return ((translatable && !translated) && !routed);
    }

//    public IUserContext getUserContext() {
//        return userContext;
//    }

//    public void setUserContext(IUserContext userContext) {
//        this.userContext = userContext;
//    }

	public Long getJobId()
    {
        return jobId;
    }

	public void setJobId(Long jobId)
    {
        this.jobId = jobId;
    }

    public Long getLegId() {
        return legId;
    }
    public void setLegId(Long legId) {
        this.legId = legId;
    }

    public boolean isRestartedDueToDeadLock() {
        return restartedDueToDeadLock;
    }

    public void setRestartedDueToDeadLock(boolean restartedDueToDeadLock) {
        this.restartedDueToDeadLock = restartedDueToDeadLock;
    }

    public boolean isFirstTransactionLeg() {
        return firstTransactionLeg;
    }

    public void setFirstTransactionLeg(boolean firstTransactionLeg) {
        this.firstTransactionLeg = firstTransactionLeg;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    @Override
    public Principal getPrincipal() {
        return principal;
    }

    @Override
    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }
}
