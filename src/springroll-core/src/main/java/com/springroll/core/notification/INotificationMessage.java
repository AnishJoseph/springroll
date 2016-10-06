package com.springroll.core.notification;

import java.time.LocalDateTime;

/**
 * Created by anishjoseph on 05/10/16.
 */
public interface INotificationMessage {
    Long getNotificationId();
    void setNotificationId(Long notificationId);
    long getCreationTime();
    void setCreationTime(long creationTime);
    String getNotificationReceivers();

}