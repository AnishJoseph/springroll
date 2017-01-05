package com.springroll.router;

import com.springroll.core.services.job.IJobStatusMessageFactory;
import com.springroll.core.services.notification.INotification;
import com.springroll.core.services.notification.INotificationMessage;
import com.springroll.core.services.notification.NotificationChannel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by anishjoseph on 05/01/17.
 */
public class JobStatusMessageFactory implements IJobStatusMessageFactory {
    @Override
    public Set<String> getTargetUsers(INotificationMessage notificationMessage) {
        Set<String> user = new HashSet<>(1);
        user.add(((JobStatusMessage)notificationMessage).getUserId());
        return user;
    }

    @Override
    public List<? extends INotification> getPendingNotificationsForUser(NotificationChannel notificationChannel) {
        return new ArrayList<>();
    }
}
