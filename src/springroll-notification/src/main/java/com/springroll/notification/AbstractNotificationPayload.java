package com.springroll.notification;



import com.springroll.core.notification.INotificationPayload;

import java.io.Serializable;

/**
 * Created by anishjoseph on 02/10/16.
 */
public abstract class AbstractNotificationPayload implements INotificationPayload, Serializable{
    private String notificationReceivers;

    public String getNotificationReceivers() {
        return notificationReceivers;
    }

    public void setNotificationReceivers(String notificationReceivers) {
        this.notificationReceivers = notificationReceivers;
    }
}
