package com.springroll.core.services.notification;

import java.util.List;
import java.util.Set;

/**
 * Created by anishjoseph on 05/10/16.
 */
public interface INotificationMessageFactory {
    Set<String> getTargetUsers(IAlertMessage notificationMessage);
    List<? extends INotification> getPendingNotificationsForUser(AlertChannel alertChannel);
}
