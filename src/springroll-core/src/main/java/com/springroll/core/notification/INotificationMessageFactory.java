package com.springroll.core.notification;

import java.util.List;
import java.util.Set;

/**
 * Created by anishjoseph on 05/10/16.
 */
public interface INotificationMessageFactory {
    Set<String> getTargetUsers(INotificationMessage notificationMessage);
    List<? extends INotification> getPendingNotificationsForUser(NotificationChannel notificationChannel);
    //FIXME - no reason why we should have a make message - its required only for alerts??
    INotificationMessage makeMessage(IReviewMeta notificationMeta);
}
