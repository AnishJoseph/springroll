package com.springroll.orm.entities;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.springroll.core.AckLog;
import com.springroll.core.notification.INotification;
import com.springroll.core.notification.INotificationMessage;
import org.hibernate.internal.util.SerializationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Version;
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
    private static final Logger logger = LoggerFactory.getLogger(Notification.class);

    private transient INotificationMessage notificationMessage;

    @Column(name = "PAYLOAD", columnDefinition = "BLOB")
    @Lob
    private byte[] payloadJson;

    @Column(name = "RECEIVERS")
    private String receivers;

    @Column(name = "USERS")
    private String users;

    @Column(name = "CHANNEL_NAME")
    private String channelName;

    @Column(name = "ACK_LOG")
    private String ackLogAsJson = "";

    @Column(name = "CREATION_TIME")
    private LocalDateTime creationTime;

    @Column(name = "INITIATOR")
    private String initiator;

    @Column(name = "AUTO_CLEAN")
    private Boolean autoClean;

    @Version
    @Column(name = "VERSION")
    private Long version;

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
        if(this.payloadJson == null) return null;
        notificationMessage = (INotificationMessage) SerializationHelper.deserialize(this.payloadJson);
        return notificationMessage;
    }

    public void setNotificationMessage(INotificationMessage notification) {
        this.payloadJson = SerializationHelper.serialize(notification);
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
        if(ackLogAsJson.isEmpty()){
            ackLog = new ArrayList<>();
            return ackLog;
        }

        if (this.ackLog == null) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            try {
                this.ackLog = mapper.readValue(ackLogAsJson, new TypeReference<List<AckLog>>(){});
            } catch (IOException e) {
                logger.debug("Exception {}" + e.getMessage());
                return new ArrayList<>();
            }
        }
        return ackLog;
    }

    public void addAck(AckLog ackLog){
        getAckLog();
        this.ackLog.add(ackLog);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            ackLogAsJson = mapper.writeValueAsString(this.ackLog);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
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

    public Boolean isAutoClean() {
        return autoClean;
    }

    public void setAutoClean(Boolean autoClean) {
        this.autoClean = autoClean;
    }
}