package com.springroll.notification;

import com.springroll.core.services.notification.PushChannel;

/**
 * Created by anishjoseph on 03/10/16.
 */
public enum CorePushChannels implements PushChannel {
    NOTIFICATION_CANCEL("/core/notificationCancel"),
    JOB_STATUS_UPDATE("/core/jobstatusupdate");

    private String serviceUri;

    CorePushChannels(String topicName) {
        this.serviceUri = topicName;
    }

    @Override public String getServiceUri() {
        return serviceUri;
    }

    @Override public String getChannelName(){
        return this.name();
    }
}

