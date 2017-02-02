package com.springroll.notification;

import com.springroll.core.services.mdm.IMdmReviewNotificationMessageFactory;
import com.springroll.core.services.notification.INotificationMessageFactory;
import com.springroll.core.services.notification.NotificationChannel;
import com.springroll.core.services.review.IFyiReviewNotificationMessageFactory;
import com.springroll.core.services.review.IReviewNotificationMessageFactory;

/**
 * Created by anishjoseph on 03/10/16.
 */
public enum CoreNotificationChannels implements NotificationChannel {
    REVIEW("/core/review", IReviewNotificationMessageFactory.class, true, false),
    FYI("/core/fyi", FyiNotificationMessageFactory.class, true, true),
    REVIEW_FYI("/core/reviewfyi", IFyiReviewNotificationMessageFactory.class, true, true),
    MDM_REVIEW("/core/mdmreview", IMdmReviewNotificationMessageFactory.class, true, false),
    SPRINGROLL_EXCEPTION("/core/springrollexception", SpringrollExceptionNotificationMessageFactory.class, true, false);

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

}
