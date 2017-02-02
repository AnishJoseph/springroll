package com.springroll.core.services.notification;

/**
 * Created by anishjoseph on 03/10/16.
 */
public interface AlertChannel {

    INotificationMessageFactory getMessageFactory();

    void setMessageFactory(INotificationMessageFactory dataMassager);

    Class<? extends INotificationMessageFactory> getMessageFactoryClass();

    String getServiceUri();

    String getChannelName();

    /**
     * If set to true the notification manager will delete the notification when all users that this notification is targeted to acknowledge it
     * If false, an explicit call to deleteNotification needs to made to delete the notification - regardless of how many users responded to the notification
     * @return
     */
    boolean isAutoClean();
}
