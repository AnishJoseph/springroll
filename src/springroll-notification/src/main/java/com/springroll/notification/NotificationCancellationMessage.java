package com.springroll.notification;

import com.springroll.core.services.notification.AlertType;

/**
 * Created by anishjoseph on 15/10/16.
 */
public class NotificationCancellationMessage extends AbstractNotificationMessage {
    protected AlertType channelType;
    public NotificationCancellationMessage(){}
    public NotificationCancellationMessage(AlertType channelType, Long id, String initiator ){
        this.channelType = channelType;
        this.setChannel(CoreNotificationChannels.NOTIFICATION_CANCEL.getChannelName());
        this.setId(id);
        setInitiator(initiator);
    }

    public AlertType getChannelType() {
        return channelType;
    }

    public void setChannelType(AlertType channelType) {
        this.channelType = channelType;
    }
}
