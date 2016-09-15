package com.springroll.api.facade;

import com.springroll.core.DTO;
import com.springroll.core.Principal;
import com.springroll.router.SynchEndPoint;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anishjoseph on 12/09/16.
 */
public abstract class AbstractAPI {
    @Autowired
    private SynchEndPoint synchEndPoint;

    public abstract Principal getPrincipal();

    public Long route(DTO payload){
        List<DTO> payloads = new ArrayList<>(1);
        payloads.add(payload);
        return route(payloads);
    }
    public Long route(List<DTO> payloads){
        return synchEndPoint.route(payloads, getPrincipal());
    }
    public Long routeSynchronouslyToAsynchronousSideFromSynchronousSide(DTO payload){
        List<DTO> payloads = new ArrayList<>(1);
        payloads.add(payload);
        return routeSynchronouslyToAsynchronousSideFromSynchronousSide(payloads);
    }
    public Long routeSynchronouslyToAsynchronousSideFromSynchronousSide(List<DTO> payloads){
        return synchEndPoint.routeSynchronous(payloads, getPrincipal());
    }

}
