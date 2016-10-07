package com.springroll.notification;

import com.springroll.core.SpringrollSecurity;
import com.springroll.core.notification.INotification;
import com.springroll.core.notification.INotificationChannel;
import com.springroll.core.notification.INotificationMessage;
import com.springroll.core.notification.INotificationMessageFactory;
import com.springroll.orm.entities.Notification;
import com.springroll.orm.entities.User;
import com.springroll.orm.repositories.Repositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by anishjoseph on 05/10/16.
 */
@Component public class FyiReviewNotificationMessageFactory implements INotificationMessageFactory {
    @Autowired Repositories repositories;

    @Override
    public Set<String> getTargetUsers(INotificationMessage notificationMessage) {
        // The notificationReceiver can either be a user or a role
        User user = repositories.users.findByUserIdIgnoreCase(notificationMessage.getNotificationReceivers());
        if(user != null){
            Set<String> users = new HashSet<>();
            users.add(user.getUserId());
            return users;
        }
        Set<String> usersThatBelongToRole = repositories.users.findUsersThatBelongToRole("%" + notificationMessage.getNotificationReceivers() + "%");

        /* Remove the initiator from the set of users to be notified */
//        usersThatBelongToRole.remove(SpringrollSecurity.getUser().getUsername());
        return usersThatBelongToRole;
    }

    @Override
    public List<? extends INotification> getPendingNotificationsForUser(INotificationChannel notificationChannel) {
        org.springframework.security.core.userdetails.User user = SpringrollSecurity.getUser();
        List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        String userId = user.getUsername();
        roles.add(userId);
        String pattern = "%\"" + userId + "\"%";
        List<Notification> notifications = repositories.notification.findByChannelNameAndInitiatorNotLikeAndAckLogAsJsonNotLikeAndReceiversIn(notificationChannel.getChannelName(), userId, pattern, roles);
        notifications.addAll(repositories.notification.findByChannelNameAndReceivers(notificationChannel.getChannelName(), userId));
        return notifications;
    }
}