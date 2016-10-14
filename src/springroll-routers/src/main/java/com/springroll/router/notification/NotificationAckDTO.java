package com.springroll.router.notification;

import com.springroll.core.DTO;
import com.springroll.core.IDTOProcessors;
import com.springroll.router.CoreDTOProcessors;

/**
 * Created by anishjoseph on 28/09/16.
 */
public class NotificationAckDTO implements DTO{
    private Long notificationId;

    public NotificationAckDTO(){}

    public NotificationAckDTO(Long notificationId) {
        this.notificationId = notificationId;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    @Override
    public IDTOProcessors getProcessor() {
        return CoreDTOProcessors.NOTIFICATION_ACK;
    }


}
