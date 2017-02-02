package com.springroll.core.services.notification;

import java.util.Set;

/**
 * Created by anishjoseph on 02/10/16.
 */
public interface NotificationService {
    void pushNotification(Set<String> targetUsers, Object notificationMessage, PushChannel pushChannel);

    Long sendNotification(NotificationChannel notificationChannel, INotificationMessage notificationPayload);

    void pushPendingNotifications(String serviceUri);

    void addNotificationChannels(Class<? extends NotificationChannel>  clazz );

    void deleteNotification(Long notificationId);

    void addNotificationAcknowledgement(Long notificationId);

    NotificationChannel nameToEnum(String enumValue);

    String getServiceInstanceForNotificationId(Long notificationId);


}
