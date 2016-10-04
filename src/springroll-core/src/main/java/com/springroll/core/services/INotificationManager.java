package com.springroll.core.services;

import com.springroll.core.notification.INotificationChannel;
import com.springroll.core.notification.INotificationPayload;

/**
 * Created by anishjoseph on 02/10/16.
 */
public interface INotificationManager {
    Long sendNotification(INotificationChannel notificationChannel, INotificationPayload notificationPayload, boolean persist, boolean sendPostCommit);

    void pushPendingNotifications(String serviceUri);

    void addNotificationChannels(Class<? extends INotificationChannel>  clazz );

    void deleteNotification(Long notificationId);

    void addNotificationAcknowledgement(Long notificationId);


}
