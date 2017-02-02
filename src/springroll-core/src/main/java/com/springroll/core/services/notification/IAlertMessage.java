package com.springroll.core.services.notification;

import java.io.Serializable;

/**
 * Created by anishjoseph on 05/10/16.
 */
public interface IAlertMessage extends Serializable{
    Long getId();
    void setId(Long notificationId);
    long getCreationTime();
    void setCreationTime(long creationTime);
    String getNotificationReceivers();
    void setChannel(String channel);
    void setInitiator(String initiator);
    String getInitiator();
    AlertType getAlertType();
    void setAlertType(AlertType alertType);

}
