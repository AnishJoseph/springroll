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

    @Column(name = "NOTIFICATION_PAYLOAD_JSON")
    private String notificationPayloadJson;

    @Column(name = "NOTIFICATION_RECEIVERS")
    private String notificationReceivers;

    @Column(name = "NOTIFICATION_CHANNEL_NAME")
    private String notificationChannelName;

    public INotificationPayload getNotificationPayload() {
        if(notificationPayload != null)return notificationPayload;
        if(notificationPayloadJson == null) return null;
        notificationPayload = (INotificationPayload) new JSONDeserializer().deserialize(notificationPayloadJson);
        return notificationPayload;
    }

    public void setNotificationPayload(INotificationPayload notification) {
        this.notificationPayload = notification;
        JSONSerializer serializer = new JSONSerializer();
        notificationPayloadJson = serializer.deepSerialize(notification);
    }

    public String getNotificationReceivers() {
        return notificationReceivers;
    }

    public void setNotificationReceivers(String notificationReceivers) {
        this.notificationReceivers = notificationReceivers;
    }

    public String getNotificationChannelName() {
        return notificationChannelName;
    }

    public void setNotificationChannelName(String notificationChannelName) {
        this.notificationChannelName = notificationChannelName;
    }
}