package com.springroll.core.notification;

import java.util.List;

/**
 * Created by anishjoseph on 30/09/16.
 */
public interface INotificationDataProvider {
    List<? extends INotificationPayload> getPendingNotifications(INotificationChannel notificationChannel);
}
