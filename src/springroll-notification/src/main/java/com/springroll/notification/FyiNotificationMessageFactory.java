package com.springroll.notification;

import com.springroll.core.AckLog;
import com.springroll.core.SpringrollSecurity;
import com.springroll.core.SpringrollUser;
import com.springroll.core.notification.*;
import com.springroll.orm.entities.Notification;
import com.springroll.orm.entities.User;
import com.springroll.orm.repositories.Repositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by anishjoseph on 05/10/16.
 */
@Component public class FyiNotificationMessageFactory implements INotificationMessageFactory {

    @Autowired Repositories repositories;

    @Override
    public Set<String> getTargetUsers(INotificationMessage notificationMessage, String initiator) {
        FyiNotificationMessage message = (FyiNotificationMessage) notificationMessage;
        // The notificationReceiver can either be a user or a role
        User user = repositories.users.findByUserIdIgnoreCase(notificationMessage.getNotificationReceivers());
        if(user != null){
            Set<String> users = new HashSet<>();
            users.add(user.getUserId());
            return users;
        }
        Set<String> usersThatBelongToRole = repositories.users.findUsersThatBelongToRole(message.getNotificationReceivers());
        return usersThatBelongToRole;
    }

    @Override
    public List<? extends INotification> getPendingNotificationsForUser(INotificationChannel notificationChannel) {
        SpringrollUser user = SpringrollSecurity.getUser();
        List<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        String userId = user.getUsername();
        List<Notification> notis = repositories.notification.findByChannelNameAndReceiversIn(notificationChannel.getChannelName(), roles);
        List<Notification> notifications = new ArrayList<>();
        for (Notification notification : notis) {
            boolean alreadyAcked = false;
            for (AckLog ackLog : notification.getAckLog()) {
                if(ackLog.getReviewer().equals(userId)){
                    alreadyAcked = true;
                    break;
                }
            }
            if(!alreadyAcked)notifications.add(notification);
        }

        return notifications;
    }

    @Override
    public INotificationMessage makeMessage(IReviewMeta notificationMeta) {
        //FIXME
        return null;
    }
}
