package com.springroll.core.services.notification;

import com.springroll.core.notification.NotificationChannel;
import com.springroll.core.notification.INotificationMessage;

/**
 * Created by anishjoseph on 02/10/16.
 */
public interface NotificationService {
    Long sendNotification(NotificationChannel notificationChannel, INotificationMessage notificationPayload);

    void pushPendingNotifications(String serviceUri);

    void addNotificationChannels(Class<? extends NotificationChannel>  clazz );

    void deleteNotification(Long notificationId);

    void addNotificationAcknowledgement(Long notificationId);

    NotificationChannel nameToEnum(String enumValue);

}
