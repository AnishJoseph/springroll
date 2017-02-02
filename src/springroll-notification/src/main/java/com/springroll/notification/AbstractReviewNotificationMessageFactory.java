package com.springroll.notification;

import com.springroll.core.SpringrollSecurity;
import com.springroll.core.SpringrollUser;
import com.springroll.core.services.notification.INotification;
import com.springroll.core.services.notification.AlertChannel;
import com.springroll.core.services.notification.IAlertMessage;
import com.springroll.orm.entities.Notification;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by anishjoseph on 05/10/16.
 */
public abstract class AbstractReviewNotificationMessageFactory extends AbstractNotificationMessageFactory {

    @Override
    public Set<String> getTargetUsers(IAlertMessage notificationMessage) {
        Set<String> targetUsers = super.getTargetUsers(notificationMessage);
        /* If the initiator is not the receiver then remove the initiator from the set of target users */
        if(!notificationMessage.getInitiator().equals(notificationMessage.getNotificationReceivers()))
            targetUsers.remove(notificationMessage.getInitiator());
        return targetUsers;
    }

    @Override
    public List<? extends INotification> getPendingNotificationsForUser(AlertChannel alertChannel) {
        SpringrollUser user = SpringrollSecurity.getUser();
        List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        String userId = user.getUsername();
        roles.add(userId);

        List<Notification> notis = repositories.notification.findByChannelNameAndInitiatorNotAndReceiversIn(alertChannel.getChannelName(), userId, roles);

        List<Notification> notifications = filterAckedNotifications(notis, userId);
        notifications.addAll(repositories.notification.findByChannelNameAndReceivers(alertChannel.getChannelName(), userId));
        return notifications;
    }
}
