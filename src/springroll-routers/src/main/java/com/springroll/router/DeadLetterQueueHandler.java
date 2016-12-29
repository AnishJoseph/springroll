package com.springroll.router;

import com.springroll.core.*;
import com.springroll.core.exceptions.DebugInfo;
import com.springroll.core.exceptions.SpringrollException;
import com.springroll.core.services.notification.PushService;
import com.springroll.orm.entities.Job;
import com.springroll.orm.repositories.Repositories;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import javax.persistence.OptimisticLockException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anishjoseph on 09/09/16.
 */
@Component
public class DeadLetterQueueHandler {
    private static final Logger logger = LoggerFactory.getLogger(DeadLetterQueueHandler.class);
    private Map<String, ExceptionCauses> causesMap = new HashMap<>();

    @Autowired JobManager jobManager;
    @Autowired AsynchSideEndPoints asynchSideEndPoints;
    @Autowired PushService pushService;
    @Autowired Repositories repositories;

    public void on(Exchange exchange, IEvent event){
        ExceptionCauses causedExceptionDetails = getCausedExceptionDetails(event);
        if(causedExceptionDetails == null)return;//FIXME - what to do here?
        Throwable caused = causedExceptionDetails.caused;
        if(isLockingIssue(caused)){
            jobManager.handleOptimisticLockFailure(event.getJobId(), event.getLegId());
            logger.debug("Deadlock detected - will sleep awhile and restart");
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            asynchSideEndPoints.routeToJms(event);
            logger.debug("Redelivering message that had deadlocked");
            return;
        }

        jobManager.handleExceptionInTransactionLeg(event.getJobId(), event.getLegId(), caused.getClass().getSimpleName());
        DebugInfo debugInfo = getDebugInfo(caused, event, causedExceptionDetails);
        SpringrollUser user = causedExceptionDetails.causingEvent.getUser();
        if(debugInfo.getSpringrollException() != null){
            String messageKey = debugInfo.getSpringrollException().getMessageKey();
            String[] messageArguments = debugInfo.getSpringrollException().getMessageArguments();
            String localizedServerMessage = LocaleFactory.getLocalizedServerMessage(user.getLocale(), messageKey, messageArguments);
            logger.error(localizedServerMessage);
            pushService.pushSpringrollExceptionNotification(debugInfo, user.getUsername(), user.getUsername());
            return;
        }
        pushService.pushSpringrollExceptionNotification(debugInfo, user.getUsername(), user.getUsername());
        caused.printStackTrace();
    }

    private DebugInfo getDebugInfo(Throwable cause, IEvent event, ExceptionCauses exceptionCauses){
        List<String> exceptions = new ArrayList<>();
        List<String> causes = new ArrayList<>();
        List<StackTraceElement> stackTraceElements = new ArrayList<>();
        SpringrollException springrollException = null;
        Throwable rootCause = cause;

        exceptions.add(cause.getClass().getSimpleName());
        causes.add(cause.getMessage());
        stackTraceElements.add(cause.getStackTrace()[0]);

        if(rootCause instanceof SpringrollException)springrollException = (SpringrollException)rootCause;

        while(rootCause.getCause() != null){
            rootCause = rootCause.getCause();
            if(rootCause instanceof SpringrollException)springrollException = (SpringrollException)rootCause;
            causes.add(rootCause.getMessage());
            exceptions.add(rootCause.getClass().getSimpleName());
            stackTraceElements.add(rootCause.getStackTrace()[0]);
        }
        DTO payload = event.getPayload();
        if(payload instanceof ServiceDTO)
            return new DebugInfo(springrollException, causes, exceptions, stackTraceElements, ((ServiceDTO)event.getPayload()).getServiceDefinition().name(), event.getClass().getSimpleName(), exceptionCauses.causingEvent.getClass().getSimpleName(),true);

        Job originalJob = repositories.job.findOne(event.getJobId());
        //FIXME - handle case where original job is NULL
        return new DebugInfo(springrollException, causes, exceptions, stackTraceElements, ((ServiceDTO) originalJob.getPayloads().get(0)).getServiceDefinition().name(), event.getClass().getSimpleName(), exceptionCauses.causingEvent.getClass().getSimpleName(), false);
    }

    public void setExceptionCauses(Throwable caused, IEvent causingEvent){
        String key =  makeKey(causingEvent);
        if(causesMap.containsKey(key))return;
        synchronized (causesMap){
            causesMap.put(key, new ExceptionCauses(causingEvent,caused));
        }
    }
    private ExceptionCauses getCausedExceptionDetails(IEvent event){
        String key =  makeKey(event);
        synchronized (causesMap){
            return causesMap.remove(key);
        }
    }

    private class ExceptionCauses{
        IEvent causingEvent;
        Throwable caused;
        public ExceptionCauses(IEvent causingEvent, Throwable caused){
            this.caused = caused;
            this.causingEvent = causingEvent;
        }
    }

    private String makeKey(IEvent event){
        return "" + event.getJobId() + ":" + event.getLegId();
    }

    /**
     * Give a Throwable this method attempts to determine if this was caused by optimistic locking (See JPA)
     * @param caused
     * @return true if caused by Optimistic Locking issues
     */
    public boolean isLockingIssue(Throwable caused){
        return  (
                caused instanceof ObjectOptimisticLockingFailureException ||
                        caused instanceof OptimisticLockException ||
                        caused instanceof CannotAcquireLockException ||
                        (caused.getMessage() != null && caused.getMessage().contains("LockAcquisitionException")) ||
                        (caused.getCause() != null && caused.getCause() instanceof ObjectOptimisticLockingFailureException) ||
                        (caused.getCause() != null && caused.getCause() instanceof OptimisticLockException) ||
                        (caused.getCause() != null && caused.getCause().getMessage() != null && caused.getCause().getMessage().contains("LockAcquisitionException"))
        );
    }

}
