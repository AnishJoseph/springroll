package com.springroll.notification;

import com.springroll.core.services.notification.AlertNotificationMessage;
import com.springroll.core.services.notification.AlertType;

/**
 * Created by anishjoseph on 05/10/16.
 */
public abstract class AbstractAlertNotificationMessage  extends AbstractNotificationMessage implements AlertNotificationMessage {
    protected AlertType channelType;

    @Override
    public AlertType getChannelType() {
        return channelType;
    }

    @Override
    public void setChannelType(AlertType channelType) {
        this.channelType = channelType;
    }
}
