package com.springroll.notification;

import com.springroll.core.*;
import com.springroll.core.exceptions.SpringrollException;
import com.springroll.core.services.notification.*;
import com.springroll.core.services.push.WebPushService;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by anishjoseph on 02/10/16.
 */
@Service
public class NotificationManager implements NotificationService, LovProvider {
    @Autowired private WebPushService pushServices;
    @Autowired private Repositories repositories;
    @Autowired private ApplicationContext applicationContext;
    @Autowired ApplicationEventPublisher publisher;
    @Autowired SpringrollUtils springrollUtils;

    private static final Logger logger = LoggerFactory.getLogger(NotificationManager.class);


    private Map<String, NotificationChannel> serviceNameToNotificationChannel = new HashMap<>();

    private List<Class> notificationChannels = new ArrayList<>();

    @PostConstruct void init(){
        this.addNotificationChannels(CoreNotificationChannels.class);
    }

    @Override public void pushNotification(Set<String> targetUsers, Object notificationMessage, PushChannel pushChannel){
        /* Use spring to publish - spring will deliver this on a successful commit of the transaction.
           This ensures that the notification is pushed to the user ONLY after the current transaction commits.
           If we don't have this, then the event is pushed to the user even if the transaction rolls back
         */
        publisher.publishEvent(new PushData(targetUsers, new Object[]{notificationMessage}, pushChannel.getServiceUri()));
    }

    @Override public Long sendNotification(NotificationChannel notificationChannel, INotificationMessage notificationMessage) {
        Set<String> targetUsers = notificationChannel.getMessageFactory().getTargetUsers(notificationMessage);
        /* Use spring to publish - spring will deliver this on a successful commit of the transaction.
           This ensures that the notification is pushed to the user ONLY after the current transaction commits.
           If we don't have this, then the event is pushed to the user even if the transaction rolls back
         */
        publisher.publishEvent(new PushData(targetUsers, new Object[]{notificationMessage}, notificationChannel.getServiceUri()));

        notificationMessage.setCreationTime(System.currentTimeMillis());
        notificationMessage.setChannel(notificationChannel.getServiceUri());

        if(!notificationChannel.isPersist())return null;

        Notification notification = new Notification();
        repositories.notification.save(notification);
        notification.setReceivers(notificationMessage.getNotificationReceivers());
        notification.setChannelName(notificationChannel.getChannelName());
        notification.setUsers(targetUsers);
        notification.setCreationTime(LocalDateTime.now());
        notification.setInitiator(notificationMessage.getInitiator());
        notification.setAutoClean(notificationChannel.isAutoClean());

        notificationMessage.setId(notification.getID());
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
        try {
            NotificationChannel notificationChannel = serviceUriToEnum(serviceUri);
            if (notificationChannel == null) {
                logger.error("Unable to find NotificationChannel for channel '{}' - probably does not exist in the Enums", serviceUri);
                return;
            }
            if (notificationChannel.getMessageFactory() == null) return;
            List<Notification> pendingNotificationsForUser = (List<Notification>) notificationChannel.getMessageFactory().getPendingNotificationsForUser(notificationChannel);
            if (pendingNotificationsForUser.isEmpty()) return;

            List<INotificationMessage> notificationMessagesForUser = pendingNotificationsForUser.stream().map(Notification::getNotificationMessage).collect(Collectors.toList());
            Set<String> user = new HashSet<>(1);
            user.add(SpringrollSecurity.getUser().getUsername());
            pushNotification(new PushData(user, notificationMessagesForUser, notificationChannel.getServiceUri()));
        }catch (Exception e){
            e.printStackTrace(); //FIXME
        }
    }

    @Override
    public void addNotificationChannels(Class<? extends NotificationChannel> clazz) {
        notificationChannels.add(clazz);
        resolveFactories(clazz);
    }

    @Override
    public void deleteNotification(Long notificationId) {
        Notification notification = repositories.notification.findOne(notificationId);
        if(notification == null){
            logger.error("Unable to find notification with id {}", notificationId);
            return;
        }
        NotificationCancellationMessage msg = new NotificationCancellationMessage(((AlertNotificationMessage)notification.getNotificationMessage()).getAlertType(), notificationId);
        pushNotification(notification.getUsers(), msg, CorePushChannels.NOTIFICATION_CANCEL);
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

    @Override
    public NotificationChannel nameToEnum(String enumValue){
        NotificationChannel notificationChannel = notificationChannels.stream()
                .flatMap(channel -> Arrays.stream(channel.getEnumConstants()))
                .filter(enumConstant -> ((Enum) enumConstant).name().equals(enumValue))
                .findFirst()
                .map(o -> (NotificationChannel)o)
                .orElse(null);          //FIXME - throw exception!!

        return notificationChannel;
    }

    @Override
    public String getServiceInstanceForNotificationId(Long notificationId) {
        Notification notification = repositories.notification.findOne(notificationId);
        if(notification == null){
            logger.error("Unable to find notification with id {}", notificationId);
            return "Unknown";
        }
        if(notification.getNotificationMessage() instanceof DismissibleNotificationMessage){
            LocalDateTime date = Instant.ofEpochMilli(notification.getNotificationMessage().getCreationTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
            return LocaleFactory.getLocalizedMessage(SpringrollSecurity.getUser().getLocale(), notification.getChannelName()) + " : " +
                    ((DismissibleNotificationMessage)notification.getNotificationMessage()).getMessage() + " at " + date.format(springrollUtils.getDateTimeFormatter());
        }
        throw new SpringrollException(null, "notification.notdismissible", notification.getChannelName(), notification.getNotificationMessage().getClass().getSimpleName());

    }

    private NotificationChannel serviceUriToEnum(String channel) {
        NotificationChannel notificationChannel = serviceNameToNotificationChannel.get(channel);
        if(notificationChannel != null)return notificationChannel;

        NotificationChannel channelForUri = notificationChannels.stream()
                .flatMap(notiChannel -> Arrays.stream(notiChannel.getEnumConstants()))
                .filter(enumConstant -> ((NotificationChannel) enumConstant).getServiceUri().equals(channel))
                .findFirst()
                .map(enumConstant -> {
                    serviceNameToNotificationChannel.put(channel, (NotificationChannel)enumConstant);
                    return (NotificationChannel) enumConstant;
                })
                .orElse(null);         //FIXME - throw exception!!

        return channelForUri;
    }

    private void resolveFactories(Class notificationChannelClass){
        Stream.of((NotificationChannel[])notificationChannelClass.getEnumConstants())
                .filter(notificationChannel -> notificationChannel.getMessageFactoryClass() != null)
                .forEach(notificationChannel -> notificationChannel.setMessageFactory(applicationContext.getBean(notificationChannel.getMessageFactoryClass())));
    }

    @Override
    public List<Lov> getLovs() {
        /* Get the Enum constants and stream it for each Notification channel and create an LOV for each enum */
        List<Lov> lovs = notificationChannels.stream()
                .flatMap(notificationChannel -> Arrays.stream(notificationChannel.getEnumConstants()))
                .map(Lov::new)
                .collect(Collectors.toList());

        return lovs;
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
