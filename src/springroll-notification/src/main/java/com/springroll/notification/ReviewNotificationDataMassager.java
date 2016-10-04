package com.springroll.notification;

import com.springroll.core.notification.INotificationPayload;
import com.springroll.core.notification.INotificationDataMassager;
import com.springroll.core.notification.MassagedNotificationData;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anishjoseph on 02/10/16.
 */
@Component public class ReviewNotificationDataMassager implements INotificationDataMassager {
    @Override
    public MassagedNotificationData massage(INotificationPayload notification) {
        MassagedNotificationData massagedNotificationData = new MassagedNotificationData();
        massagedNotificationData.setData(notification);
        massagedNotificationData.setNotificationReceivers("BOM");
        List<String> users = new ArrayList<>();
        users.add("ANISH");
        massagedNotificationData.setUsers(users);
        return massagedNotificationData;
    }
}
