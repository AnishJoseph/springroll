package com.springroll.core.services.notification;

import com.springroll.core.notification.INotificationChannel;
import com.springroll.core.notification.INotificationMessage;

/**
 * Created by anishjoseph on 02/10/16.
 */
public interface NotificationService {
    Long sendNotification(INotificationChannel notificationChannel, INotificationMessage notificationPayload);

    void pushPendingNotifications(String serviceUri);

    void addNotificationChannels(Class<? extends INotificationChannel>  clazz );

    void deleteNotification(Long notificationId);

    void addNotificationAcknowledgement(Long notificationId);

    INotificationChannel nameToEnum(String enumValue);

}
