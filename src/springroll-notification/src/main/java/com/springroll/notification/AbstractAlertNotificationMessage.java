package com.springroll.notification;

import com.springroll.core.services.notification.AlertNotificationMessage;
import com.springroll.core.services.notification.NotificationChannelType;

/**
 * Created by anishjoseph on 05/10/16.
 */
public abstract class AbstractAlertNotificationMessage  extends AbstractNotificationMessage implements AlertNotificationMessage {
    protected NotificationChannelType channelType;

    @Override
    public NotificationChannelType getChannelType() {
        return channelType;
    }

    @Override
    public void setChannelType(NotificationChannelType channelType) {
        this.channelType = channelType;
    }
}
