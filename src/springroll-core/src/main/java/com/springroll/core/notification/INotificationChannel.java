package com.springroll.core.notification;

/**
 * Created by anishjoseph on 03/10/16.
 */
public interface INotificationChannel {

    INotificationMessageFactory getMessageFactory();

    void setMessageFactory(INotificationMessageFactory dataMassager);

    Class<? extends INotificationMessageFactory> getMessageFactoryClass();

    String getServiceUri();

    String getChannelName();
}
