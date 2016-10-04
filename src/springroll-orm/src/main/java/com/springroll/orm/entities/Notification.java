package com.springroll.orm.entities;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.springroll.core.AckLog;
import com.springroll.core.ReviewLog;
import com.springroll.core.notification.INotificationPayload;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    private transient List<AckLog> ackLog;
    @Column(name = "ACK_LOG")
    private String ackLogAsJson;

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

    public List<AckLog> getAckLog() {
        if (this.ackLog == null && ackLogAsJson != null) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            try {
                this.ackLog = mapper.readValue(ackLogAsJson, new TypeReference<List<ReviewLog>>(){});
            } catch (IOException e) {
                return null;
            }
        }
        return ackLog;
    }

    public void setAckLog(List<AckLog> ackLog) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        this.ackLog = ackLog;
        try {
            ackLogAsJson = mapper.writeValueAsString(ackLog);
        } catch (JsonProcessingException e) {
            //FIXME
        }
    }
    public void addAck(AckLog ackLog){
        getAckLog();
        if(this.ackLog == null) this.ackLog = new ArrayList<>();
        this.ackLog.add(ackLog);
        this.setAckLog(this.ackLog);
    }

    public boolean hasThisUserAlreadyAcknowledged(String  userId){
        List<AckLog> ackLog = getAckLog();
        if(ackLog == null)return false;
        for (AckLog data : ackLog) {
            if(data.getReviewer().equals(userId))return true;
        }
        return false;

    }


}