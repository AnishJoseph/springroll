package com.springroll.core.services.notification;

/**
 * Created by anishjoseph on 05/10/16.
 */
public interface DismissibleNotificationMessage extends INotificationMessage{
    /*  When a notification is dismissed by a user we need to store this action with the actual message that was shown to the user (or something
        that lets the user know which notification was dismissed (in the Job Dashboard). Without a meaningful message the user will just see that
        a Notification was deleted - not which one. getMessage() is invoked when a notification is deleted and the result is store in the Job table
     */
    String getMessage();
}
