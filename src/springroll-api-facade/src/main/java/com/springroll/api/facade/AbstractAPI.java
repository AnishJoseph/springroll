package com.springroll.api.facade;

import com.springroll.core.DTO;
import com.springroll.core.Principal;
import com.springroll.orm.entities.Job;
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
    public Long routeSynchronous(DTO payload){
        List<DTO> payloads = new ArrayList<>(1);
        payloads.add(payload);
        return routeSynchronous(payloads);
    }
    public Long routeSynchronous(List<DTO> payloads){
        return synchEndPoint.routeSynchronous(payloads, getPrincipal());
    }

}
