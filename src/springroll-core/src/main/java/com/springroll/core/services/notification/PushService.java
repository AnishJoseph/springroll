package com.springroll.core.services.notification;

/**
 * This interface abstracts the notification manager - it is used to hide the notification manager
 * and provide high level methods to push data down specific notification channels
 * Created by anishjoseph on 21/12/16.
 */
public interface PushService {
    /**
     * Push a message on the FYI channel
     * @param messageKey - the message key (reference to a key in ui.messages)
     * @param messageArguments - arguments to the message
     * @param notificationReceivers - either a user or a role
     * @return the notification ID
     */
    Long pushFYINotification(String messageKey, String[] messageArguments, String notificationReceivers);
    Long pushSpringrollExceptionNotification(String messageKey, String[] messageArguments, String notificationReceivers, String initiator);
}
