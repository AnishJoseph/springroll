package com.springroll.notification;

import com.springroll.core.services.INotificationManager;
import com.springroll.core.services.notification.PushService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by anishjoseph on 21/12/16.
 */
@Service
public class PushManager implements PushService {
    @Autowired INotificationManager notificationManager;
    @Override
    public Long pushFYINotification(String messageKey, String[] messageArguments, String notificationReceivers) {
        FyiNotificationMessage message = new FyiNotificationMessage(messageKey, messageArguments, notificationReceivers);
        return notificationManager.sendNotification(CoreNotificationChannels.FYI, message);
    }
}
