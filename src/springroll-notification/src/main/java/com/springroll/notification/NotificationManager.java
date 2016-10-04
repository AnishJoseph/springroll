package com.springroll.notification;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        this.addNotificationChannels(InternalNotificationChannels.class);
    }

    @Override public Long sendNotification(INotificationChannel notificationChannel, INotificationPayload notificationPayload, boolean persist, boolean sendPostCommit) {
        MassagedNotificationData massagedNotificationData = notificationChannel.getDataMassager().massage(notificationPayload);
        if(massagedNotificationData == null || massagedNotificationData.getUsers().isEmpty()){
            return null;
        }
        if(sendPostCommit){
            /* This ensures that the notification is pushed to the user ONLY after the current transaction commits. If we dont have this then the
               event is pushed to the even if the transaction rolls back
             */
            publisher.publishEvent(new PushData(massagedNotificationData.getUsers(), massagedNotificationData.getData(), notificationChannel.getServiceUri()));
        } else {

            pushNotification(new PushData(massagedNotificationData.getUsers(), massagedNotificationData.getData(), notificationChannel.getServiceUri()));
        }

        if(!persist)return null;

        Notification notification = new Notification();
        notification.setNotificationPayload(notificationPayload);
        notification.setNotificationReceivers(massagedNotificationData.getNotificationReceivers());
        notification.setNotificationChannelName(notificationChannel.getChannelName());
        repositories.notification.save(notification);
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
        List<? extends INotificationPayload> pendingNotifications = notificationChannel.getDataProvider().getPendingNotifications(notificationChannel);
        for (INotificationPayload pendingNotification : pendingNotifications) {
            sendNotification(notificationChannel, pendingNotification, false, false);
        }
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
            enumConstant.setDataMassager(applicationContext.getBean(enumConstant.getDataMassagerClass()));
            enumConstant.setDataProvider(applicationContext.getBean(enumConstant.getDataProviderClass()));
        }
    }

    private class PushData{
        private List<String> users;
        private Object data;
        private String serviceUri;

        public PushData(List<String> users, Object data, String serviceUri) {
            this.users = users;
            this.data = data;
            this.serviceUri = serviceUri;
        }

    }
}
