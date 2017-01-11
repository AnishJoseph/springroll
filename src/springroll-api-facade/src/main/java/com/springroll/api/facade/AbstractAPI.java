package com.springroll.api.facade;

import com.springroll.core.*;
import com.springroll.core.exceptions.SpringrollException;
import com.springroll.orm.repositories.Repositories;
import com.springroll.router.JobMeta;
import com.springroll.router.SynchEndPoint;
import com.springroll.router.exceptions.BusinessValidationException;
import com.springroll.router.exceptions.PropertyValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by anishjoseph on 12/09/16.
 */
public abstract class AbstractAPI {
    @Autowired private SynchEndPoint synchEndPoint;
    @Autowired protected LocaleFactory localeFactory;
    protected static Locale _locale = Locale.getDefault();
    @Autowired Repositories repositories;
    @PersistenceContext EntityManager em;
    private static final Logger logger = LoggerFactory.getLogger(AbstractAPI.class);



    @ExceptionHandler(PropertyValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleValidationException(PropertyValidationException ex) {
        return propertyViolationsAsModelAndView(ex.getViolations());
    }

    @ExceptionHandler(BusinessValidationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public  List<BusinessValidationViolations> handleBusinessValidationException(BusinessValidationException ex) {
        List<BusinessValidationViolations> violations =
                ex.getViolations().stream()
                        .map(businessValidationResult -> new BusinessValidationViolations(businessValidationResult.getCookie(), businessValidationResult.getDtoIndex(), businessValidationResult.getField(),
                                                                LocaleFactory.getLocalizedServerMessage(getUser().getLocale(), businessValidationResult.getMessageKey(), businessValidationResult.getArgs())))
                        .collect(Collectors.toList());

        return violations;
    }
    @ExceptionHandler(SpringrollException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public  String handleSpringrollException(SpringrollException ex) {
        logger.error(LocaleFactory.getLocalizedServerMessage(_locale, ex.getMessageKey(), ex.getMessageArguments()));
        return LocaleFactory.getLocalizedServerMessage(getUser().getLocale(), ex.getMessageKey(), ex.getMessageArguments());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public  String handleRuntimeException(RuntimeException ex) {
        Throwable caused = ex;
        while(caused.getCause() != null)caused = caused.getCause();
        caused.printStackTrace();
        return caused.getMessage() == null ? "System Error : " + caused.getClass().getSimpleName() : "System Error : " + caused.getMessage();
    }

    public Long route(ServiceDTO payload){
        List<ServiceDTO> payloads = new ArrayList<>(1);
        payloads.add(payload);
        return route(payloads);
    }
    public Long route(List<? extends ServiceDTO> payloads){
        JobMeta jobMeta = new JobMeta(payloads, null, true);
        return sendItDownTheSynchronousRoute(jobMeta);
    }

    public Long routeSynchronouslyToAsynchronousSideFromSynchronousSide(ServiceDTO payload){
        List<ServiceDTO> payloads = new ArrayList<>(1);
        payloads.add(payload);
        return routeSynchronouslyToAsynchronousSideFromSynchronousSide(payloads);
    }

    public Long routeSynchronouslyToAsynchronousSideFromSynchronousSide(List<? extends ServiceDTO> payloads){
        JobMeta jobMeta = new JobMeta(payloads, null, false);
        return sendItDownTheSynchronousRoute(jobMeta);
    }

    private Long sendItDownTheSynchronousRoute(JobMeta jobMeta){
        return synchEndPoint.route(jobMeta);
    }

    private SpringrollUser getUser(){
        return SpringrollSecurity.getUser();
    }

    public ModelAndView propertyViolationsAsModelAndView(Map<String,Map<String,String>> violations) {
        MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
        return new ModelAndView(jsonView, violations);
    }

}
