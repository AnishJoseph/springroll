package com.springroll.notification;

import com.springroll.core.notification.INotificationChannel;
import com.springroll.core.notification.INotificationDataMassager;
import com.springroll.core.notification.INotificationDataProvider;

/**
 * Created by anishjoseph on 03/10/16.
 */
public enum InternalNotificationChannels implements INotificationChannel {
    REVIEW("/core/review", ReviewNotificationDataProvider.class, ReviewNotificationDataMassager.class);
    private String serviceUri;
    private INotificationDataProvider dataProvider = null;
    private INotificationDataMassager dataMassager = null;
    private Class<? extends INotificationDataProvider> dataProviderClass;
    private Class<? extends INotificationDataMassager> dataMassagerClass;

    InternalNotificationChannels(String topicName, Class<? extends INotificationDataProvider> dataProviderClass, Class<? extends INotificationDataMassager> dataMassagerClass) {
        this.serviceUri = topicName;
        this.dataProviderClass = dataProviderClass;
        this.dataMassagerClass = dataMassagerClass;
    }

    @Override public INotificationDataProvider getDataProvider() {
        return dataProvider;
    }

    @Override public INotificationDataMassager getDataMassager() {
        return dataMassager;
    }

    @Override public String getServiceUri() {
        return serviceUri;
    }

    @Override public String getChannelName(){
        return this.name();
    }

    @Override public void setDataProvider(INotificationDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override public void setDataMassager(INotificationDataMassager dataMassager) {
        this.dataMassager = dataMassager;
    }

    @Override public Class<? extends INotificationDataProvider> getDataProviderClass() {
        return dataProviderClass;
    }

    @Override public Class<? extends INotificationDataMassager> getDataMassagerClass() {
        return dataMassagerClass;
    }
}
