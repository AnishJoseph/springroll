package com.springroll.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by anishjoseph on 09/09/16.
 */
public class ContextStore {
    private static final Logger logger = LoggerFactory.getLogger(ContextStore.class);
    private static final ThreadLocal<ContextAttributes> threadScopeAttributesHolder = new InheritableThreadLocal<ContextAttributes>();

    public static void put(SpringrollUser user, Long jobId, Long legId){
        ContextAttributes scopeAttributes = new ContextAttributes();
        scopeAttributes.setUser(user);
        scopeAttributes.setJobId(jobId);
        scopeAttributes.setLegId(legId);
        threadScopeAttributesHolder.set(scopeAttributes);
    }

    public static SpringrollUser getUser() {
        if(threadScopeAttributesHolder.get() != null)
            return threadScopeAttributesHolder.get().getUser();
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
