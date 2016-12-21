package com.springroll.router;

import com.springroll.core.services.notification.NotificationService;
import com.springroll.router.notification.NotificationAckEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by anishjoseph on 04/10/16.
 */
@Component public class CoreEventsEndPoint extends SpringrollEndPoint {
    @Autowired
    NotificationService notificationService;

    public void on(NotificationAckEvent event) {
        //FIXME - check if this user allowed to ack this notification??
        notificationService.addNotificationAcknowledgement(event.getPayload().getNotificationId());
    }
}
