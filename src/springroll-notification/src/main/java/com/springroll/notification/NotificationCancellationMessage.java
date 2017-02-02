package com.springroll.notification;

import com.springroll.core.services.notification.AlertType;

/**
 * Created by anishjoseph on 15/10/16.
 */
public class NotificationCancellationMessage  {
    private AlertType alertType;
    private Long id;
    public NotificationCancellationMessage(){}
    public NotificationCancellationMessage(AlertType alertType, Long id ){
        this.alertType = alertType;
        this.id = id;
    }

    public AlertType getAlertType() {
        return alertType;
    }

    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
