package com.springroll.api.facade;

import com.springroll.core.*;
import com.springroll.core.exceptions.FixableException;
import com.springroll.orm.repositories.Repositories;
import com.springroll.router.JobMeta;
import com.springroll.router.SynchEndPoint;
import com.springroll.router.exceptions.BusinessValidationException;
import com.springroll.router.exceptions.PropertyValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by anishjoseph on 12/09/16.
 */
public abstract class AbstractAPI {
    @Autowired private SynchEndPoint synchEndPoint;
    @Autowired protected LocaleFactory localeFactory;
    protected static Locale _locale = Locale.getDefault();
    @Autowired Repositories repositories;


    @ExceptionHandler(PropertyValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleValidationException(PropertyValidationException ex) {
        return propertyViolationsAsModelAndView(ex.getViolations());
    }

    @ExceptionHandler(BusinessValidationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public  List<BusinessValidationResult> handleBusinessValidationException(BusinessValidationException ex) {
        return ex.getViolations();
    }
    @ExceptionHandler(FixableException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public  String handleFixableException(FixableException ex) {
        return LocaleFactory.getLocalizedServerMessage(_locale, ex.getMessageKey(), ex.getMessageArguments());
    }

    public Long route(ServiceDTO payload){
        List<ServiceDTO> payloads = new ArrayList<>(1);
        payloads.add(payload);
        return route(payloads);
    }
    public Long route(List<? extends ServiceDTO> payloads){
        JobMeta jobMeta = new JobMeta(payloads, getUser(), null, null, null, false, true);
        return sendItDownTheSynchronousRoute(jobMeta);
    }

    public Long routeSynchronouslyToAsynchronousSideFromSynchronousSide(ServiceDTO payload){
        List<ServiceDTO> payloads = new ArrayList<>(1);
        payloads.add(payload);
        return routeSynchronouslyToAsynchronousSideFromSynchronousSide(payloads);
    }

    public Long routeSynchronouslyToAsynchronousSideFromSynchronousSide(List<? extends ServiceDTO> payloads){
        JobMeta jobMeta = new JobMeta(payloads, getUser(), null, null, null, false, false);
        return sendItDownTheSynchronousRoute(jobMeta);
    }

    private Long sendItDownTheSynchronousRoute(JobMeta jobMeta){
        /* This point, at the start of the flow, we only have the principal to store - the jobId and legId is created in EventCreator */
        ContextStore.put(jobMeta.getUser(), null, null);
        return synchEndPoint.route(jobMeta);
    }

    private SpringrollUser getUser(){
        return (SpringrollUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public ModelAndView propertyViolationsAsModelAndView(Map<String,Map<String,String>> violations) {
        MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
        return new ModelAndView(jsonView, violations);
    }

}
