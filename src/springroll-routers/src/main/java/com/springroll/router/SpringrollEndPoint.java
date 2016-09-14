package com.springroll.router;

import com.springroll.core.DTO;
import com.springroll.core.IEvent;
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
        event.setJobId(JmsEntryPoint.getJobId());
        event.setLegId(JmsEntryPoint.getLegId());
        event.setPrincipal(JmsEntryPoint.getPrincipal());
        asynchSideEndPoints.routeInSameTransaction(event);
    }

    public Long routeToSynchronousSide(List<? extends DTO> payloads){
        return synchEndPoint.routeSynchronous(payloads, JmsEntryPoint.getJobId());
    }
}
