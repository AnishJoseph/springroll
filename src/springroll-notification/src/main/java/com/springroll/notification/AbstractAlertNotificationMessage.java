package com.springroll.notification;

import com.springroll.core.services.notification.AlertMessage;
import com.springroll.core.services.notification.AlertType;

/**
 * Created by anishjoseph on 05/10/16.
 */
public abstract class AbstractAlertNotificationMessage  extends AbstractAlertMessage implements AlertMessage {
    protected AlertType alertType;

    public AlertType getAlertType() {
        return alertType;
    }

    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }
}
