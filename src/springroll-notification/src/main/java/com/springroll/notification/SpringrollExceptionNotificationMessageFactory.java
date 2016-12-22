package com.springroll.notification;

import com.springroll.core.SpringrollSecurity;
import com.springroll.core.services.notification.INotification;
import com.springroll.core.services.notification.NotificationChannel;
import com.springroll.orm.entities.Notification;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by anishjoseph on 05/10/16.
 */
@Component public class SpringrollExceptionNotificationMessageFactory extends AbstractNotificationMessageFactory {
    @Override
    public List<? extends INotification> getPendingNotificationsForUser(NotificationChannel notificationChannel) {
        //FIXME - also get messages for this users role
        List<Notification> pendingNotificationsForUser = repositories.notification.findByChannelNameAndReceivers(notificationChannel.getChannelName(), SpringrollSecurity.getUser().getUsername());
        return filterAckedNotifications(pendingNotificationsForUser, SpringrollSecurity.getUser().getUsername());
    }

}