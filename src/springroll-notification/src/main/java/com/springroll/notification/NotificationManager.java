package com.springroll.notification;

import com.springroll.core.AckLog;
import com.springroll.core.SpringrollSecurity;
import com.springroll.core.notification.*;
import com.springroll.core.services.INotificationManager;
import com.springroll.orm.entities.Notification;
import com.springroll.orm.repositories.Repositories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by anishjoseph on 02/10/16.
 */
@Service
public class NotificationManager implements INotificationManager {
    @Autowired private PushServices pushServices;
    @Autowired private Repositories repositories;
    @Autowired private ApplicationContext applicationContext;
    @Autowired ApplicationEventPublisher publisher;

    private static final Logger logger = LoggerFactory.getLogger(NotificationManager.class);


    private Map<String, INotificationChannel> serviceNameToNotificationChannel = new HashMap<>();

    private List<Class> notificationChannels = new ArrayList<>();

    @PostConstruct void init(){
        this.addNotificationChannels(CoreNotificationChannels.class);
    }

    @Override public Long sendNotification(INotificationChannel notificationChannel, INotificationMessage notificationMessage) {
        Set<String> targetUsers = notificationChannel.getMessageFactory().getTargetUsers(notificationMessage);
        /* Use spring to publish - spring will deliver this on a successful commit of the transaction.
           This ensures that the notification is pushed to the user ONLY after the current transaction commits.
           If we don't have this, then the event is pushed to the user even if the transaction rolls back
         */
        publisher.publishEvent(new PushData(targetUsers, new Object[]{notificationMessage}, notificationChannel.getServiceUri()));

        if(!notificationChannel.isPersist())return null;

        Notification notification = new Notification();
        repositories.notification.save(notification);
        notification.setReceivers(notificationMessage.getNotificationReceivers());
        notification.setChannelName(notificationChannel.getChannelName());
        notification.setUsers(targetUsers);
        notification.setCreationTime(LocalDateTime.now());
        notification.setInitiator(SpringrollSecurity.getUser().getUsername());
        notification.setAutoClean(notificationChannel.isAutoClean());

        notificationMessage.setCreationTime(System.currentTimeMillis());
        notificationMessage.setId(notification.getID());
        notificationMessage.setChannel(notificationChannel.getServiceUri());
        /* This MUST be last as we are setting stuff in the notification message before this */
        notification.setNotificationMessage(notificationMessage);
        return notification.getID();

    }
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void pushNotification(PushData pushData) {
        pushServices.deliver(pushData.users, pushData.data, pushData.serviceUri);
    }

    @Override
    public void pushPendingNotifications(String serviceUri) {
        INotificationChannel notificationChannel = serviceUriToEnum(serviceUri);
        if(notificationChannel == null){
            logger.error("Unable to find NotificationChannel for channel '{}' - probably does not exist in the Enums", serviceUri);
            return;
        }
        List<Notification> pendingNotificationsForUser = (List<Notification>)notificationChannel.getMessageFactory().getPendingNotificationsForUser(notificationChannel);
        if(pendingNotificationsForUser.isEmpty())return;

        List<INotificationMessage> notificationMessagesForUser = pendingNotificationsForUser.stream().map(Notification::getNotificationMessage).collect(Collectors.toList());
        Set<String> user = new HashSet<>(1);
        user.add(SpringrollSecurity.getUser().getUsername());
        pushNotification(new PushData(user, notificationMessagesForUser, notificationChannel.getServiceUri()));
    }

    @Override
    public void addNotificationChannels(Class<? extends INotificationChannel> clazz) {
        notificationChannels.add(clazz);
        fixBeans(clazz);
    }

    @Override
    public void deleteNotification(Long notificationId) {
        repositories.notification.delete(notificationId);
    }

    @Override
    public void addNotificationAcknowledgement(Long notificationId) {
        Notification notification = repositories.notification.findOne(notificationId);
        if(notification == null){
            logger.error("Acknowledgement for non-existent notification with id '{}' attempted by user {}", notificationId, SpringrollSecurity.getUser().getUsername());
            return;
        }
        if(!notification.getUsers().contains(SpringrollSecurity.getUser().getUsername())){
            logger.error("Illegal acknowledgement for notification with id '{}' - this notification is not targeted to user {}", notificationId, SpringrollSecurity.getUser().getUsername());
            return;
        }
        notification.addAck(new AckLog(SpringrollSecurity.getUser().getUsername(), LocalDateTime.now()));
        if(notification.isAutoClean()){
            if(notification.getAckLog().size() == notification.getUsers().size()){
                //Assuming here that if the sizes are the same then all users have acknowledged
                repositories.notification.delete(notificationId);
            }
        }
    }

    private INotificationChannel serviceUriToEnum(String channel) {
        INotificationChannel notificationChannel = serviceNameToNotificationChannel.get(channel);
        if(notificationChannel != null)return notificationChannel;

        for (Class notificationChannelClass : notificationChannels) {
            INotificationChannel[] enumConstants = (INotificationChannel[])notificationChannelClass.getEnumConstants();
            for (INotificationChannel enumConstant : enumConstants) {
                if(enumConstant.getServiceUri().equals(channel)){
                    serviceNameToNotificationChannel.put(channel, enumConstant);
                    return enumConstant;
                }
            }
        }
        return null;
    }
    private void fixBeans(Class notificationChannelClass){
        INotificationChannel[] enumConstants = (INotificationChannel[])notificationChannelClass.getEnumConstants();
        for (INotificationChannel enumConstant : enumConstants) {
            //FIXME - what if we cant find the bean
            enumConstant.setMessageFactory(applicationContext.getBean(enumConstant.getMessageFactoryClass()));
        }
    }

    private class PushData{
        private Set<String> users;
        private Object data;
        private String serviceUri;

        public PushData(Set<String> users, Object data, String serviceUri) {
            this.users = users;
            this.data = data;
            this.serviceUri = serviceUri;
        }
    }
}
