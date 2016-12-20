package com.springroll.notification;

import com.springroll.core.notification.NotificationChannelType;

/**
 * Created by anishjoseph on 15/10/16.
 */
public class NotificationCancellationMessage extends AbstractNotificationMessage {
    public NotificationCancellationMessage(){}
    public NotificationCancellationMessage(NotificationChannelType notificationChannelType, Long id, String initiator ){
        this.setChannelType(notificationChannelType);
        this.setChannel(CoreNotificationChannels.NOTIFICATION_CANCEL.getChannelName());
        this.setId(id);
        setInitiator(initiator);
    }
}
