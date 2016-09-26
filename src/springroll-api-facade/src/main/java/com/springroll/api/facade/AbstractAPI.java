package com.springroll.api.facade;

import com.springroll.core.DTO;
import com.springroll.core.ContextStore;
import com.springroll.router.JobMeta;
import com.springroll.router.SynchEndPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by anishjoseph on 12/09/16.
 */
public abstract class AbstractAPI {
    @Autowired
    private SynchEndPoint synchEndPoint;

    public Long route(DTO payload){
        List<DTO> payloads = new ArrayList<>(1);
        payloads.add(payload);
        return route(payloads);
    }
    public Long route(List<DTO> payloads){
        JobMeta jobMeta = new JobMeta(payloads, getUser(), null, null, null, true, true);
        return sendItDownTheSynchronousRoute(jobMeta);
    }
    public Long routeSynchronouslyToAsynchronousSideFromSynchronousSide(DTO payload){
        List<DTO> payloads = new ArrayList<>(1);
        payloads.add(payload);
        return routeSynchronouslyToAsynchronousSideFromSynchronousSide(payloads);
    }
    public Long routeSynchronouslyToAsynchronousSideFromSynchronousSide(List<? extends DTO> payloads){
        JobMeta jobMeta = new JobMeta(payloads, getUser(), null, null, null, true, false);
        return sendItDownTheSynchronousRoute(jobMeta);
    }

    private Long sendItDownTheSynchronousRoute(JobMeta jobMeta){
        /* This point, at the start of the flow, we only have the principal to store - the jobId and legId is created in EventCreator */
        ContextStore.put(jobMeta.getUser(), null, null);
        return synchEndPoint.route(jobMeta);
    }

    private User getUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public ModelAndView propertyViolationsAsModelAndView(Map<String,Map<String,String>> violations) {
        MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
        return new ModelAndView(jsonView, violations);
    }

}
