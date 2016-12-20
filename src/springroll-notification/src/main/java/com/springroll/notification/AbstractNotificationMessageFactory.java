package com.springroll.notification;

import com.springroll.core.AckLog;
import com.springroll.core.SpringrollSecurity;
import com.springroll.core.SpringrollUser;
import com.springroll.core.notification.INotification;
import com.springroll.core.notification.INotificationChannel;
import com.springroll.core.notification.INotificationMessage;
import com.springroll.core.notification.INotificationMessageFactory;
import com.springroll.orm.entities.Notification;
import com.springroll.orm.entities.User;
import com.springroll.orm.repositories.Repositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by anishjoseph on 05/10/16.
 */
public abstract class AbstractNotificationMessageFactory implements INotificationMessageFactory {
    @Autowired protected Repositories repositories;

    @Override
    public Set<String> getTargetUsers(INotificationMessage notificationMessage) {
        // The notificationReceiver can either be a user or a role
        User user = repositories.users.findByUserIdIgnoreCase(notificationMessage.getNotificationReceivers());
        if(user != null){
            Set<String> users = new HashSet<>();
            users.add(user.getUserId());
            return users;
        }
        Set<String> usersThatBelongToRole = repositories.users.findUsersThatBelongToRole(notificationMessage.getNotificationReceivers());

        /* Remove the initiator from the set of users to be notified */
        return usersThatBelongToRole;
    }

    @Override
    public List<? extends INotification> getPendingNotificationsForUser(INotificationChannel notificationChannel) {
        SpringrollUser user = SpringrollSecurity.getUser();
        List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        List<Notification> notis = repositories.notification.findByChannelNameAndReceiversIn(notificationChannel.getChannelName(), roles);
        return filterAckedNotifications(notis, user.getUsername());
    }

    protected List<Notification> filterAckedNotifications(List<Notification> notifications, String userId){
        List<Notification> pendingNotifications = new ArrayList<>();
        for (Notification notification : notifications) {
            boolean alreadyAcked = false;
            for (AckLog ackLog : notification.getAckLog()) {
                if(ackLog.getReviewer().equals(userId)){
                    alreadyAcked = true;
                    break;
                }
            }
            if(!alreadyAcked)pendingNotifications.add(notification);
        }
        return  notifications;
    }
}
