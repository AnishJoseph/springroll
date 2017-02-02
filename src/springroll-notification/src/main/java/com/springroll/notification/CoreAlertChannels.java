package com.springroll.notification;

import com.springroll.core.services.mdm.IMdmReviewNotificationMessageFactory;
import com.springroll.core.services.notification.INotificationMessageFactory;
import com.springroll.core.services.notification.AlertChannel;
import com.springroll.core.services.review.IFyiReviewNotificationMessageFactory;
import com.springroll.core.services.review.IReviewNotificationMessageFactory;

/**
 * Created by anishjoseph on 03/10/16.
 */
public enum CoreAlertChannels implements AlertChannel {
    REVIEW("/alerts/core/review", IReviewNotificationMessageFactory.class, false),
    FYI("/alerts/core/fyi", FyiNotificationMessageFactory.class, true),
    REVIEW_FYI("/alerts/core/reviewfyi", IFyiReviewNotificationMessageFactory.class, true),
    MDM_REVIEW("/alerts/core/mdmreview", IMdmReviewNotificationMessageFactory.class, false),
    SPRINGROLL_EXCEPTION("/alerts/core/springrollexception", SpringrollExceptionNotificationMessageFactory.class, true);

    private String serviceUri;
    private INotificationMessageFactory messageFactory = null;
    private boolean autoClean;
    private Class<? extends INotificationMessageFactory> messageFactoryClass;

    CoreAlertChannels(String topicName, Class<? extends INotificationMessageFactory> messageFactoryClass, boolean autoClean) {
        this.serviceUri = topicName;
        this.messageFactoryClass = messageFactoryClass;
        this.autoClean = autoClean;
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

    @Override public boolean isAutoClean() {
        return autoClean;
    }

}
