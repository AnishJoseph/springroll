package com.springroll.orm.entities;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.springroll.core.AckLog;
import com.springroll.core.ReviewLog;
import com.springroll.core.notification.INotification;
import com.springroll.core.notification.INotificationMessage;
import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import org.hibernate.annotations.Type;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by anishjoseph on 05/09/16.
 */

@Entity
public class Notification extends AbstractEntity implements INotification {

    private transient INotificationMessage notificationMessage;

    @Column(name = "PAYLOAD")
    private String payloadJson;

    @Column(name = "RECEIVERS")
    private String receivers;

    @Column(name = "USERS")
    private String users;

    @Column(name = "CHANNEL_NAME")
    private String channelName;

    @Column(name = "ACK_LOG")
    private String ackLogAsJson;

    @Column(name = "CREATION_TIME")
    @Type(type="com.springroll.orm.LocalDateTimeUserType")
    private LocalDateTime creationTime;

    @Column(name = "INITIATOR")
    private String initiator;


    private transient List<AckLog> ackLog;
    private transient Set<String> userList;


    public Set<String> getUsers() {
        if(userList == null)userList = StringUtils.commaDelimitedListToSet(users);
        return userList;
    }

    public void setUsers(Set<String> users) {
        this.users = StringUtils.collectionToCommaDelimitedString(users);
        this.userList = users;
    }

    public INotificationMessage getNotificationMessage() {
        if(notificationMessage != null)return notificationMessage;
        if(payloadJson == null) return null;
        notificationMessage = (INotificationMessage) new JSONDeserializer().deserialize(payloadJson);
        return notificationMessage;
    }

    public void setNotificationMessage(INotificationMessage notification) {
        this.notificationMessage = notification;
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

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }
}