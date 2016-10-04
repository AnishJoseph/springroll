package com.springroll.core.notification;

import java.util.List;

/**
 * Created by anishjoseph on 02/10/16.
 */
public class MassagedNotificationData {
    private List<String> users;
    private String notificationReceivers;
    private Object data;

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getNotificationReceivers() {
        return notificationReceivers;
    }

    public void setNotificationReceivers(String notificationReceivers) {
        this.notificationReceivers = notificationReceivers;
    }
}
