package com.springroll.notification;

import com.springroll.core.notification.INotificationMessage;

/**
 * Created by anishjoseph on 05/10/16.
 */
public class AbstractNotificationMessage implements INotificationMessage {
    private long creationTime;
    private Long notificationId;
    private String notificationReceivers;

    public String getNotificationReceivers() {
        return notificationReceivers;
    }

    public void setNotificationReceivers(String notificationReceivers) {
        this.notificationReceivers = notificationReceivers;
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }
}
