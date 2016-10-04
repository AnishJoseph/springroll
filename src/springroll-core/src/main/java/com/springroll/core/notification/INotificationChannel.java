package com.springroll.core.notification;

/**
 * Created by anishjoseph on 03/10/16.
 */
public interface INotificationChannel {
    INotificationDataProvider getDataProvider();

    INotificationDataMassager getDataMassager();

    void setDataProvider(INotificationDataProvider dataProvider);

    void setDataMassager(INotificationDataMassager dataMassager);

    Class<? extends INotificationDataMassager> getDataMassagerClass();

    Class<? extends INotificationDataProvider> getDataProviderClass();

    String getServiceUri();

    String getChannelName();
}
