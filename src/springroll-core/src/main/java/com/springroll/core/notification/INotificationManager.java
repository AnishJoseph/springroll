package com.springroll.core.notification;

/**
 * Created by anishjoseph on 02/10/16.
 */
public interface INotificationManager {
    void sendNotification(INotificationChannel notificationChannel, INotificationPayload notificationPayload, boolean persist, boolean sendPostCommit);

    void pushPendingNotifications(String serviceUri);

    void addNotificationChannels(Class<? extends INotificationChannel>  clazz );
}
