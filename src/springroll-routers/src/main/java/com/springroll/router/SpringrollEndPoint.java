package com.springroll.router;

import com.springroll.core.DTO;
import com.springroll.core.IEvent;
import com.springroll.core.UserContextFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
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
        event.setJobId(UserContextFactory.getJobId());
        event.setLegId(UserContextFactory.getLegId());
        event.setPrincipal(UserContextFactory.getPrincipal());
        asynchSideEndPoints.routeInSameTransaction(event);
    }

    public Long routeToSynchronousSideFromAsynchronousSide(List<? extends DTO> payloads){
        return synchEndPoint.routeSynchronous(payloads, UserContextFactory.getJobId(), UserContextFactory.getPrincipal());
    }
}
