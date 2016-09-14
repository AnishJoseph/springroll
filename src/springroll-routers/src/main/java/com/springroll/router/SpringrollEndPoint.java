package com.springroll.router;

import com.springroll.core.IEvent;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by anishjoseph on 10/09/16.
 */
public abstract class SpringrollEndPoint {
    @Autowired
    AsynchSideEndPoints asynchSideEndPoints;

    public void route(IEvent event){
        asynchSideEndPoints.routeInSameTransaction(event);
    }
}
