package com.springroll.notification;

import com.springroll.core.SpringrollSecurity;
import com.springroll.core.notification.*;
import com.springroll.orm.entities.Notification;
import com.springroll.orm.entities.Users;
import com.springroll.orm.repositories.Repositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by anishjoseph on 05/10/16.
 */
@Component public class ReviewNotificationMessageFactory implements INotificationMessageFactory {
    @Autowired Repositories repositories;

    @Override
    public Set<String> getTargetUsers(INotificationMessage notificationMessage) {
        ReviewNotificationMessage reviewNotificationMessage = (ReviewNotificationMessage) notificationMessage;
        // The notificationReceiver can either be a user or a group
        Users user = repositories.users.findByUserIdIgnoreCase(notificationMessage.getNotificationReceivers());
        if(user != null){
            Set<String> users = new HashSet<>();
            users.add(user.getUserId());
            return users;
        }
        Set<String> usersThatBelongToGroup = repositories.users.findUsersThatBelongToGroup("%" + notificationMessage.getNotificationReceivers() + "%");

        /* Remove the initiator from the group of users to be notified */
        usersThatBelongToGroup.remove(SpringrollSecurity.getUser().getUsername());
        return usersThatBelongToGroup;
    }

    @Override
    public List<? extends INotification> getPendingNotificationsForUser(INotificationChannel notificationChannel) {
        User user = SpringrollSecurity.getUser();
        List<String> groups = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        String userId = user.getUsername();
        groups.add(userId);
        String pattern = "%\"" + userId + "\"%";
        List<Notification> notifications = repositories.notification.findByChannelNameAndInitiatorNotLikeAndAckLogAsJsonNotLikeAndReceiversIn(notificationChannel.getChannelName(), userId, pattern, groups);
        return notifications;
    }
}