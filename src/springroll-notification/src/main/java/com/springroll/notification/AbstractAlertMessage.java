package com.springroll.notification;

import com.springroll.core.services.notification.AlertType;
import com.springroll.core.services.notification.IAlertMessage;

/**
 * Created by anishjoseph on 05/10/16.
 */
public abstract class AbstractAlertMessage implements IAlertMessage {
    private long creationTime;
    private Long id;
    private String notificationReceivers;
    private String channel;
    private String initiator;
    protected AlertType alertType;

    @Override
    public AlertType getAlertType() {
        return alertType;
    }

    @Override
    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }

    @Override
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

}
