package com.springroll.router;

import com.springroll.core.IEvent;
import com.springroll.core.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by anishjoseph on 09/09/16.
 */
public class JmsEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(JmsEntryPoint.class);
    private static final ThreadLocal<AsynchSideContextAttributes> threadScopeAttributesHolder = new InheritableThreadLocal<AsynchSideContextAttributes>();

    public static void on(IEvent event) {
        /* Called when the event comes out from JMS - it will be in a new thread so set these attributes for the new thread */
        setP1Context(event.getPrincipal(), event.getJobId(), event.getLegId());
    }
    public static void setP1Context(Principal principal, Long jobId, Long xActionLegId){
        AsynchSideContextAttributes scopeAttributes = new AsynchSideContextAttributes();
        scopeAttributes.setPrincipal(principal);
        scopeAttributes.setJobId(jobId);
        scopeAttributes.setLegId(xActionLegId);
        threadScopeAttributesHolder.set(scopeAttributes);
    }

    public static void resetP1Context(){
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
