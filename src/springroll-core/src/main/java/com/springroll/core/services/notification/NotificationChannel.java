package com.springroll.core.services.notification;

/**
 * Created by anishjoseph on 03/10/16.
 */
public interface NotificationChannel {

    INotificationMessageFactory getMessageFactory();

    void setMessageFactory(INotificationMessageFactory dataMassager);

    Class<? extends INotificationMessageFactory> getMessageFactoryClass();

    String getServiceUri();

    String getChannelName();

    /**
     * Should this notification be persisted?
     * If false, the notification will only be delivered to users that are currently logged at the time of notification (and will NOT be redelivered when the user logs in again)
     * If true, then the notification will be persisted and delivered to the user at login
     * As an example - this will be set to true for review notifications as it MUST be delivered to the user regardless of whether the user was logged in or not.
     *
     * @return
     */
    boolean isPersist();

    /**
     * If set to true the notification manager will delete the notification when all users that this notification is targeted to acknowledge it
     * If false, an explicit call to deleteNotification needs to made to delete the notfication - regardless of how many users responded to the notification
     * @return
     */
    boolean isAutoClean();
}
