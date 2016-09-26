package com.springroll.router;

import com.springroll.core.DTO;
import com.springroll.core.IEvent;
import com.springroll.core.ContextStore;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by anishjoseph on 10/09/16.
 */
public abstract class SpringrollEndPoint {
    @Autowired
    private AsynchSideEndPoints asynchSideEndPoints;

    @Autowired
    private SynchEndPoint synchEndPoint;

    public void route(IEvent event){
        event.setJobId(ContextStore.getJobId());
        event.setLegId(ContextStore.getLegId());
        event.setUser(ContextStore.getUser());
        asynchSideEndPoints.routeToDynamicRouter(event);
    }

    public Long routeToSynchronousSideFromAsynchronousSide(List<? extends DTO> payloads){
        JobMeta jobMeta = new JobMeta(payloads, ContextStore.getUser(), ContextStore.getJobId(), ContextStore.getLegId(), null, true, false);
        return synchEndPoint.route(jobMeta);
    }
}
