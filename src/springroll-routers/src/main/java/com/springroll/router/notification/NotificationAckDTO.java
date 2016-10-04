package com.springroll.router.notification;

import com.springroll.core.DTO;

/**
 * Created by anishjoseph on 28/09/16.
 */
public class NotificationAckDTO implements DTO{
    private Long notificationId;

    public NotificationAckDTO(){}

    public NotificationAckDTO(Long notificationId, Long reviewStepId, boolean approved) {
        this.notificationId = notificationId;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

}
