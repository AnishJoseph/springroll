package com.springroll.notification;

import com.springroll.core.notification.INotificationChannel;
import com.springroll.core.notification.INotificationMessageFactory;

/**
 * Created by anishjoseph on 03/10/16.
 */
public enum CoreNotificationChannels implements INotificationChannel {
    REVIEW("/core/review", ReviewNotificationMessageFactory.class),
    FYI("/core/fyi", FyiNotificationMessageFactory.class);

    private String serviceUri;
    private INotificationMessageFactory messageFactory = null;
    private Class<? extends INotificationMessageFactory> messageFactoryClass;

    CoreNotificationChannels(String topicName, Class<? extends INotificationMessageFactory> messageFactoryClass) {
        this.serviceUri = topicName;
        this.messageFactoryClass = messageFactoryClass;
    }

    @Override public INotificationMessageFactory getMessageFactory() {
        return messageFactory;
    }

    @Override public void setMessageFactory(INotificationMessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }

    @Override public Class<? extends INotificationMessageFactory> getMessageFactoryClass() {
        return messageFactoryClass;
    }

    public void setMessageFactoryClass(Class<? extends INotificationMessageFactory> messageFactoryClass) {
        this.messageFactoryClass = messageFactoryClass;
    }

    @Override public String getServiceUri() {
        return serviceUri;
    }

    @Override public String getChannelName(){
        return this.name();
    }

}
