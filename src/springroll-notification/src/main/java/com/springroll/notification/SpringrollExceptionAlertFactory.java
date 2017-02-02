package com.springroll.notification;

import com.springroll.core.SpringrollSecurity;
import com.springroll.core.services.notification.INotification;
import com.springroll.core.services.notification.AlertChannel;
import com.springroll.orm.entities.Notification;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by anishjoseph on 05/10/16.
 */
@Component public class SpringrollExceptionAlertFactory extends AbstractNotificationMessageFactory {
    @Override
    public List<? extends INotification> getPendingAlertsForUser(AlertChannel alertChannel) {
        //FIXME - also get messages for this users role
        List<Notification> pendingNotificationsForUser = repositories.notification.findByChannelNameAndReceivers(alertChannel.getChannelName(), SpringrollSecurity.getUser().getUsername());
        return filterAckedNotifications(pendingNotificationsForUser, SpringrollSecurity.getUser().getUsername());
    }

}