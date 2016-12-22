package com.springroll.notification;

/**
 * Created by anishjoseph on 05/10/16.
 */
public class SpringrollExceptionNotificationMessage extends AbstractNotificationMessage {
    private String messageKey;
    private String[] args = new String[]{};

    public SpringrollExceptionNotificationMessage() {
    }

    public SpringrollExceptionNotificationMessage(String messageKey, String[] args, String notificationReceivers, String initiator) {
        this.messageKey = messageKey;
        this.args = args;
        setNotificationReceivers(notificationReceivers);
        setInitiator(initiator);
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
