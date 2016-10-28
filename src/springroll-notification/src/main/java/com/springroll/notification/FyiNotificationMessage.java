package com.springroll.notification;

import java.util.List;

/**
 * Created by anishjoseph on 05/10/16.
 */
public class FyiNotificationMessage extends AbstractNotificationMessage {
    private String messageKey;
    private String[] args = new String[]{};

    public FyiNotificationMessage() {
    }

    public FyiNotificationMessage(String messageKey, String[] args, String notificationReceivers) {
        this.messageKey = messageKey;
        this.args = args;
        setNotificationReceivers(notificationReceivers);
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }
}
