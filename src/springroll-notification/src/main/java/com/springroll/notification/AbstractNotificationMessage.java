package com.springroll.notification;

import com.springroll.core.services.notification.INotificationMessage;
import com.springroll.core.services.notification.NotificationChannelType;

/**
 * Created by anishjoseph on 05/10/16.
 */
public abstract class AbstractNotificationMessage implements INotificationMessage {
    private long creationTime;
    private Long id;
    private String notificationReceivers;
    private String channel;
    private NotificationChannelType channelType;
    private String initiator;

    public String getNotificationReceivers() {
        return notificationReceivers;
    }

    public void setNotificationReceivers(String notificationReceivers) {
        this.notificationReceivers = notificationReceivers;
    }

    @Override
    public String getInitiator() {
        return initiator;
    }

    @Override
    public void setInitiator(String initiator) {
        this.initiator = initiator;
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

    @Override public NotificationChannelType getChannelType() {
        return channelType;
    }

    public void setChannelType(NotificationChannelType channelType) {
        this.channelType = channelType;
    }
}
