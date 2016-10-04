package com.springroll.notification;

import com.springroll.core.SpringrollSecurity;
import com.springroll.core.notification.INotificationDataMassager;
import com.springroll.core.notification.INotificationPayload;
import com.springroll.core.notification.MassagedNotificationData;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anishjoseph on 02/10/16.
 */
@Component public class FyiNotificationDataMassager implements INotificationDataMassager {
    @Override
    public MassagedNotificationData massage(INotificationPayload notification) {
        MassagedNotificationData massagedNotificationData = new MassagedNotificationData();
        massagedNotificationData.setData(notification);
        massagedNotificationData.setNotificationReceivers(notification.getNotificationReceivers());
        List<String> users = new ArrayList<>();
        users.add(SpringrollSecurity.getUser().getUsername());
        massagedNotificationData.setUsers(users);
        return massagedNotificationData;
    }
}
