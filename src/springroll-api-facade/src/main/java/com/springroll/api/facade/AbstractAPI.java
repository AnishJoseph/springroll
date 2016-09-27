package com.springroll.api.facade;

import com.springroll.core.BusinessValidationResult;
import com.springroll.core.DTO;
import com.springroll.core.ContextStore;
import com.springroll.router.JobMeta;
import com.springroll.router.SynchEndPoint;
import com.springroll.router.exceptions.BusinessValidationException;
import com.springroll.router.exceptions.OverrideableBusinessValidationException;
import com.springroll.router.exceptions.PropertyValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    @ExceptionHandler(PropertyValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleValidationException(PropertyValidationException ex) {
        return propertyViolationsAsModelAndView(ex.getViolations());
    }

    @ExceptionHandler(BusinessValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public  List<BusinessValidationResult> handleBusinessValidationException(BusinessValidationException ex) {
        return ex.getViolations();
    }

    @ExceptionHandler(OverrideableBusinessValidationException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public  List<BusinessValidationResult> handleBusinessValidationException(OverrideableBusinessValidationException ex) {
        return ex.getViolations();
    }

    public Long route(DTO payload){
        List<DTO> payloads = new ArrayList<>(1);
        payloads.add(payload);
        return route(payloads);
    }
    public Long route(List<DTO> payloads){
        JobMeta jobMeta = new JobMeta(payloads, getUser(), null, null, null, false, true);
        return sendItDownTheSynchronousRoute(jobMeta);
    }
    public Long routeAgain(DTO payload){
        List<DTO> payloads = new ArrayList<>(1);
        payloads.add(payload);
        return routeAgain(payloads);
    }
    public Long routeAgain(List<DTO> payloads){
        JobMeta jobMeta = new JobMeta(payloads, getUser(), null, null, null, true, true);
        return sendItDownTheSynchronousRoute(jobMeta);
    }
    public Long routeSynchronouslyToAsynchronousSideFromSynchronousSide(DTO payload){
        List<DTO> payloads = new ArrayList<>(1);
        payloads.add(payload);
        return routeSynchronouslyToAsynchronousSideFromSynchronousSide(payloads);
    }
    public Long routeSynchronouslyToAsynchronousSideFromSynchronousSide(List<? extends DTO> payloads){
        JobMeta jobMeta = new JobMeta(payloads, getUser(), null, null, null, false, false);
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
