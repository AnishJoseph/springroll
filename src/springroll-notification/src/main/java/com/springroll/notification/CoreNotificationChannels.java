package com.springroll.notification;

import com.springroll.core.notification.INotificationChannel;
import com.springroll.core.notification.INotificationMessageFactory;
import com.springroll.core.notification.NotificationChannelType;
import com.springroll.core.services.review.IFyiReviewNotificationMessageFactory;
import com.springroll.core.services.mdm.IMdmReviewNotificationMessageFactory;
import com.springroll.core.services.review.IReviewNotificationMessageFactory;

/**
 * Created by anishjoseph on 03/10/16.
 */
public enum CoreNotificationChannels implements INotificationChannel {
    REVIEW("/core/review", IReviewNotificationMessageFactory.class, true, false, NotificationChannelType.ACTION),
    FYI("/core/fyi", FyiNotificationMessageFactory.class, true, true, NotificationChannelType.INFO),
    REVIEW_FYI("/core/reviewfyi", IFyiReviewNotificationMessageFactory.class, true, true, NotificationChannelType.INFO),
    NOTIFICATION_CANCEL("/core/notificationCancel", null, true, true, NotificationChannelType.INFO),
    MDM_REVIEW("/core/mdmreview", IMdmReviewNotificationMessageFactory.class, true, false, NotificationChannelType.ACTION);

    private String serviceUri;
    private INotificationMessageFactory messageFactory = null;
    private boolean persist;
    private boolean autoClean;
    private NotificationChannelType channelType;
    private Class<? extends INotificationMessageFactory> messageFactoryClass;

    CoreNotificationChannels(String topicName, Class<? extends INotificationMessageFactory> messageFactoryClass, boolean persist, boolean autoClean, NotificationChannelType channelType) {
        this.serviceUri = topicName;
        this.messageFactoryClass = messageFactoryClass;
        this.autoClean = autoClean;
        this.persist = persist;
        this.channelType = channelType;
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

    @Override public String getServiceUri() {
        return serviceUri;
    }

    @Override public String getChannelName(){
        return this.name();
    }

    @Override public boolean isPersist() {
        return persist;
    }

    @Override public boolean isAutoClean() {
        return autoClean;
    }

    @Override
    public NotificationChannelType getChannelType() {
        return channelType;
    }

}
