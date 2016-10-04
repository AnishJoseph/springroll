package com.springroll.core.notification;

/**
 * Created by anishjoseph on 30/09/16.
 */
public interface INotificationDataMassager {
    MassagedNotificationData massage(INotificationPayload notification);
}
