package com.springroll.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by anishjoseph on 09/09/16.
 */
public class UserContextFactory {
    private static final Logger logger = LoggerFactory.getLogger(UserContextFactory.class);
    private static final ThreadLocal<AsynchSideContextAttributes> threadScopeAttributesHolder = new InheritableThreadLocal<AsynchSideContextAttributes>();

    public static void on(IEvent event) {
        /* Called when the event comes from JMS - it will be in a new thread so set these attributes for the new thread */
        setUserContextInThreadScope(event.getPrincipal(), event.getJobId(), event.getLegId());
    }
    public static void setUserContextInThreadScope(Principal principal, Long jobId, Long legId){
        AsynchSideContextAttributes scopeAttributes = new AsynchSideContextAttributes();
        scopeAttributes.setPrincipal(principal);
        scopeAttributes.setJobId(jobId);
        scopeAttributes.setLegId(legId);
        threadScopeAttributesHolder.set(scopeAttributes);
    }

    public static void resetUserContextInThreadScope(){
        threadScopeAttributesHolder.set(null);
    }
    public static Principal getPrincipal() {
        if(threadScopeAttributesHolder.get() != null)
            return threadScopeAttributesHolder.get().getPrincipal();
        return null;
    }
    public static Long getJobId() {
        if(threadScopeAttributesHolder.get() != null)
            return threadScopeAttributesHolder.get().getJobId();
        return null;
    }
    public static Long getLegId() {
        if(threadScopeAttributesHolder.get() != null)
            return threadScopeAttributesHolder.get().getLegId();
        return null;
    }
}
