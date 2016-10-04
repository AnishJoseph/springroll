package com.springroll.notification;



import java.util.List;

/**
 * Created by anishjoseph on 02/10/16.
 */
public class FyiNotificationPayload extends AbstractNotificationPayload {
    private String messageKey;
    private List<String> args;

    public FyiNotificationPayload() {
    }

    public FyiNotificationPayload(String messageKey, List<String> args, String notificationReceivers) {
        this.messageKey = messageKey;
        this.args = args;
        super.setNotificationReceivers(notificationReceivers);
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public List<String> getArgs() {
        return args;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }
}
