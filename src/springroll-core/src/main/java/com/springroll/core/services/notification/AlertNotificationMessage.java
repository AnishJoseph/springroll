package com.springroll.core.services.notification;

/**
 * Created by anishjoseph on 05/10/16.
 */
public interface AlertNotificationMessage {
    NotificationChannelType getChannelType();
    void setChannelType(NotificationChannelType channelType);
}
