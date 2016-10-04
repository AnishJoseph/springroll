package com.springroll.orm.entities;


import com.springroll.core.notification.INotificationPayload;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by anishjoseph on 05/09/16.
 */

@Entity
public class Notification extends AbstractEntity {

    private transient INotificationPayload notificationPayload;

    @Column(name = "PAYLOAD")
    private String payloadJson;

    @Column(name = "RECEIVERS")
    private String receivers;

    @Column(name = "CHANNEL_NAME")
    private String channelName;

    public INotificationPayload getNotificationPayload() {
        if(notificationPayload != null)return notificationPayload;
        if(payloadJson == null) return null;
        notificationPayload = (INotificationPayload) new JSONDeserializer().deserialize(payloadJson);
        return notificationPayload;
    }

    public void setNotificationPayload(INotificationPayload notification) {
        this.notificationPayload = notification;
        JSONSerializer serializer = new JSONSerializer();
        payloadJson = serializer.deepSerialize(notification);
    }

    public String getReceivers() {
        return receivers;
    }

    public void setReceivers(String receivers) {
        this.receivers = receivers;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}