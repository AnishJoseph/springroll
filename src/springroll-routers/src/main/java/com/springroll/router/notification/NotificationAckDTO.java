package com.springroll.router.notification;

import com.springroll.core.ServiceDefinition;
import com.springroll.core.ServiceDTO;
import com.springroll.router.CoreServiceDefinitions;

/**
 * Created by anishjoseph on 28/09/16.
 */
public class NotificationAckDTO implements ServiceDTO {
    private Long notificationId;
    private String serviceInstance;

    public NotificationAckDTO(){}

    public NotificationAckDTO(Long notificationId, String serviceInstance) {
        this.serviceInstance = serviceInstance;
        this.notificationId = notificationId;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    @Override
    public ServiceDefinition getServiceDefinition() {
        return CoreServiceDefinitions.NOTIFICATION_ACK;
    }

    @Override
    public String getServiceInstance() {
        return serviceInstance;
    }


}
