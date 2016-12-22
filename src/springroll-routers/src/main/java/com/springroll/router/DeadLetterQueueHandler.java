package com.springroll.router;

import com.springroll.core.IEvent;
import com.springroll.core.LocaleFactory;
import com.springroll.core.SpringrollUser;
import com.springroll.core.exceptions.SpringrollException;
import com.springroll.core.services.notification.PushService;
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
    @Autowired
    JobManager jobManager;
    @Autowired
    AsynchSideEndPoints asynchSideEndPoints;
    @Autowired
    PushService pushService;
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
        while(caused.getCause() != null)caused = caused.getCause();
        SpringrollUser user = causedExceptionDetails.causingEvent.getUser();
        if(caused instanceof SpringrollException){
            String messageKey = ((SpringrollException) caused).getMessageKey();
            String[] messageArguments = ((SpringrollException) caused).getMessageArguments();
            String localizedServerMessage = LocaleFactory.getLocalizedServerMessage(user.getLocale(), messageKey, messageArguments);
            logger.error(localizedServerMessage);
            pushService.pushSpringrollExceptionNotification(messageKey, messageArguments, user.getUsername(), user.getUsername());
            return;
        }
        caused = causedExceptionDetails.caused;
        List<String> causes = new ArrayList<>();
        causes.add(caused.getMessage());
        while(caused.getCause() != null){
            caused = caused.getCause();
            causes.add(caused.getMessage());
        }
        String allCauses = String.join("::", causes);
        pushService.pushSpringrollExceptionNotification(allCauses, new String[]{}, user.getUsername(), user.getUsername());
        logger.error(allCauses);
        caused.printStackTrace();
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
