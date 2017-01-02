package com.springroll.router;

import com.springroll.core.IEvent;
import com.springroll.core.LocaleFactory;
import com.springroll.core.ServiceDTO;
import com.springroll.core.SpringrollUser;
import com.springroll.core.exceptions.DebugInfo;
import com.springroll.core.exceptions.ExceptionCauses;
import com.springroll.core.exceptions.ExceptionStore;
import com.springroll.core.exceptions.SpringrollException;
import com.springroll.core.services.notification.PushService;
import com.springroll.orm.entities.Job;
import com.springroll.orm.repositories.Repositories;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.data.convert.JodaTimeConverters;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import javax.persistence.OptimisticLockException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by anishjoseph on 09/09/16.
 */
@Component
public class DeadLetterQueueHandler {
    private static final Logger logger = LoggerFactory.getLogger(DeadLetterQueueHandler.class);

    @Autowired JobManager jobManager;
    @Autowired AsynchSideEndPoints asynchSideEndPoints;
    @Autowired PushService pushService;
    @Autowired Repositories repositories;

    public void on(Exchange exchange, IEvent event){
        ExceptionCauses causedExceptionDetails = ExceptionStore.getCausedExceptionDetails(event);

        if(causedExceptionDetails == null)
            return;//FIXME - what to do here?
        Job originalJob = repositories.job.findOne(event.getJobId());
        String serviceName = ((ServiceDTO) originalJob.getPayloads().get(0)).getServiceDefinition().name();
        String firstEventInThisTransaction = event.getClass().getSimpleName();
        String eventThatCausedException = null;
        if(causedExceptionDetails.causingEvent == null) {
            /*  If the exception occurs during the JTA commit (after the camel route is complete) there will be no
                specific event that can be pointed to as the event that caused the rollback. In this case the event
                is marked as indeterminate
             */
            eventThatCausedException = "Indeterminate";
        } else {
            eventThatCausedException = causedExceptionDetails.causingEvent.getClass().getSimpleName();
        }
        Throwable caused = causedExceptionDetails.caused;
        if(isLockingIssue(caused)){
            jobManager.handleOptimisticLockFailure(event.getJobId(), event.getLegId());
            logger.debug("Deadlock detected - will sleep awhile and restart");
            try {
                Thread.sleep(10000);    //FIXME
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            asynchSideEndPoints.routeToJms(event);
            logger.debug("Redelivering message that had deadlocked");
            return;
        }

        jobManager.handleExceptionInTransactionLeg(event.getJobId(), event.getLegId(), caused.getClass().getSimpleName());
        DebugInfo debugInfo = getDebugInfo(caused, eventThatCausedException, event.getJobId(), event.getLegId(), serviceName, firstEventInThisTransaction);
        printStack(caused, debugInfo);
        SpringrollUser user = event.getUser();
        if(debugInfo.getSpringrollException() != null){
            String messageKey = debugInfo.getSpringrollException().getMessageKey();
            String[] messageArguments = debugInfo.getSpringrollException().getMessageArguments();
            String localizedServerMessage = LocaleFactory.getLocalizedServerMessage(user.getLocale(), messageKey, messageArguments);
            logger.error(localizedServerMessage);
            pushService.pushSpringrollExceptionNotification(debugInfo, user.getUsername(), user.getUsername());
            return;
        }
        pushService.pushSpringrollExceptionNotification(debugInfo, user.getUsername(), user.getUsername());
    }

    private DebugInfo getDebugInfo(Throwable cause, String eventThatCausedException, Long jobId, Long transactionLegId, String serviceName, String firstEventInThisTransaction){
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
        return new DebugInfo(springrollException, causes, exceptions, stackTraceElements, serviceName, firstEventInThisTransaction, eventThatCausedException, jobId, transactionLegId);
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

    public void printStack(Throwable caused, DebugInfo debugInfo){
        StringWriter errors = new StringWriter();
        if(debugInfo.getSpringrollException() == null)
            caused.printStackTrace(new PrintWriter(errors));
        long id = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append("\n------------------- Start of exception - AS:" + id + " -------------------");
        sb.append("\nService Name : " + debugInfo.getServiceName());
        sb.append("\n1st event in this Transaction : " + debugInfo.getServiceEventName());
        sb.append("\nException Event Context : " + debugInfo.getEventThatCausedException());
        sb.append("\nJob ID : " + debugInfo.getJobId());
        sb.append("\nTransaction Leg ID : " + debugInfo.getTransactionLegId());
        if(debugInfo.getSpringrollException() != null){
            String localizedServerMessage = LocaleFactory.getLocalizedServerMessage(Locale.getDefault(), debugInfo.getSpringrollException().getMessageKey(), debugInfo.getSpringrollException().getMessageArguments());
            sb.append("\nException Message : " + localizedServerMessage);
        }
        if(debugInfo.getSpringrollException() == null)
            sb.append("\nStack trace is \n" + errors);
        sb.append("\n------------------- End of exception - AS:" + id + " -------------------");
        logger.error(sb.toString());

    }

}
