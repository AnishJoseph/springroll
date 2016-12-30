package com.springroll.router;

import com.springroll.core.IEvent;
import com.springroll.core.ContextStore;
import com.springroll.core.ServiceDTO;
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

    public Long routeToSynchronousSideFromAsynchronousSide(List<? extends ServiceDTO> payloads){
        JobMeta jobMeta = new JobMeta(payloads, null, false);
        return synchEndPoint.route(jobMeta);
    }
}
