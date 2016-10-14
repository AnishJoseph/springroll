package com.springroll.core.notification;

import java.time.LocalDateTime;

/**
 * Created by anishjoseph on 05/10/16.
 */
public interface INotificationMessage {
    Long getId();
    void setId(Long notificationId);
    long getCreationTime();
    void setCreationTime(long creationTime);
    String getNotificationReceivers();
    void setChannel(String channel);

}
