package com.springroll.notification;

import com.springroll.core.notification.INotificationChannel;
import com.springroll.core.notification.INotificationMessageFactory;

/**
 * Created by anishjoseph on 03/10/16.
 */
public enum CoreNotificationChannels implements INotificationChannel {
    REVIEW("/core/review", ReviewNotificationMessageFactory.class, true, false),
    FYI("/core/fyi", FyiNotificationMessageFactory.class, true, true);

    private String serviceUri;
    private INotificationMessageFactory messageFactory = null;
    private boolean persist;
    private boolean autoClean;

    private Class<? extends INotificationMessageFactory> messageFactoryClass;

    CoreNotificationChannels(String topicName, Class<? extends INotificationMessageFactory> messageFactoryClass, boolean persist, boolean autoClean) {
        this.serviceUri = topicName;
        this.messageFactoryClass = messageFactoryClass;
        this.autoClean = autoClean;
        this.persist = persist;
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

    public boolean isPersist() {
        return persist;
    }

    public void setPersist(boolean persist) {
        this.persist = persist;
    }

    public boolean isAutoClean() {
        return autoClean;
    }

    public void setAutoClean(boolean autoClean) {
        this.autoClean = autoClean;
    }
}
