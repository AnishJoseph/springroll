package com.springroll.notification;

import com.springroll.core.services.notification.AlertType;

/**
 * Created by anishjoseph on 15/10/16.
 */
public class NotificationCancellationMessage extends AbstractNotificationMessage {
    protected AlertType alertType;
    public NotificationCancellationMessage(){}
    public NotificationCancellationMessage(AlertType alertType, Long id, String initiator ){
        this.alertType = alertType;
        this.setChannel(CoreNotificationChannels.NOTIFICATION_CANCEL.getChannelName());
        this.setId(id);
        setInitiator(initiator);
    }

    public AlertType getAlertType() {
        return alertType;
    }

    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }
}
