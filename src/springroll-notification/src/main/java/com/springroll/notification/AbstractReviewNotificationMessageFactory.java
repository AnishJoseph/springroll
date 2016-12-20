package com.springroll.notification;

import com.springroll.core.SpringrollSecurity;
import com.springroll.core.SpringrollUser;
import com.springroll.core.notification.INotification;
import com.springroll.core.notification.INotificationChannel;
import com.springroll.core.notification.INotificationMessage;
import com.springroll.orm.entities.Notification;
import com.springroll.orm.repositories.Repositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by anishjoseph on 05/10/16.
 */
public abstract class AbstractReviewNotificationMessageFactory extends AbstractNotificationMessageFactory {
    @Autowired protected Repositories repositories;

    @Override
    public Set<String> getTargetUsers(INotificationMessage notificationMessage) {
        Set<String> targetUsers = super.getTargetUsers(notificationMessage);
        /* If the initiator is not the receiver then remove the initiator from the set of target users */
        if(!notificationMessage.getInitiator().equals(notificationMessage.getNotificationReceivers()))
            targetUsers.remove(notificationMessage.getInitiator());
        return targetUsers;
    }

    @Override
    public List<? extends INotification> getPendingNotificationsForUser(INotificationChannel notificationChannel) {
        SpringrollUser user = SpringrollSecurity.getUser();
        List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        String userId = user.getUsername();
        roles.add(userId);

        List<Notification> notis = repositories.notification.findByChannelNameAndInitiatorNotAndReceiversIn(notificationChannel.getChannelName(), userId, roles);

        List<Notification> notifications = filterAckedNotifications(notis, userId);
        notifications.addAll(repositories.notification.findByChannelNameAndReceivers(notificationChannel.getChannelName(), userId));
        return notifications;
    }
}
