package com.springroll.notification;

import com.springroll.core.services.notification.NotificationChannelType;

/**
 * Created by anishjoseph on 15/10/16.
 */
public class NotificationCancellationMessage extends AbstractNotificationMessage {
    protected NotificationChannelType channelType;
    public NotificationCancellationMessage(){}
    public NotificationCancellationMessage(NotificationChannelType channelType, Long id, String initiator ){
        this.channelType = channelType;
        this.setChannel(CoreNotificationChannels.NOTIFICATION_CANCEL.getChannelName());
        this.setId(id);
        setInitiator(initiator);
    }

    public NotificationChannelType getChannelType() {
        return channelType;
    }

    public void setChannelType(NotificationChannelType channelType) {
        this.channelType = channelType;
    }
}
