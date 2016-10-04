package com.springroll.notification;

import com.springroll.core.SpringrollSecurity;
import com.springroll.core.SpringrollUser;
import com.springroll.core.notification.INotificationChannel;
import com.springroll.core.notification.INotificationDataProvider;
import com.springroll.core.notification.INotificationPayload;
import com.springroll.orm.entities.Notification;
import com.springroll.orm.repositories.Repositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by anishjoseph on 02/10/16.
 */
@Component public class FyiNotificationDataProvider implements INotificationDataProvider {

    @Autowired private Repositories repositories;

    public List<? extends INotificationPayload> getPendingNotifications(INotificationChannel notificationChannel) {
        SpringrollUser springrollUser = (SpringrollUser) SpringrollSecurity.getUser();
        List<Notification> notifications = repositories.notification.findNotificationsForUser(notificationChannel.getChannelName(), springrollUser.getUsername(), springrollUser.getGroups());
        List<INotificationPayload> notificationPayloads = new ArrayList<>();
        try {
            for (Notification notification : notifications) {
                notificationPayloads.add(notification.getNotificationPayload());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return notificationPayloads;

    }
}
