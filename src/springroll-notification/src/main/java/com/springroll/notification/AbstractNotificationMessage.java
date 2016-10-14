package com.springroll.notification;

import com.springroll.core.notification.INotificationMessage;

/**
 * Created by anishjoseph on 05/10/16.
 */
public class AbstractNotificationMessage implements INotificationMessage {
    private long creationTime;
    private Long id;
    private String notificationReceivers;
    private String channel;

    public String getNotificationReceivers() {
        return notificationReceivers;
    }

    public void setNotificationReceivers(String notificationReceivers) {
        this.notificationReceivers = notificationReceivers;
    }

    @Override public long getCreationTime() {
        return creationTime;
    }

    @Override public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    @Override public Long getId() {
        return id;
    }

    @Override public void setId(Long id) {
        this.id = id;
    }

    public String getChannel() {
        return channel;
    }

    @Override public void setChannel(String channel) {
        this.channel = channel;
    }
}
