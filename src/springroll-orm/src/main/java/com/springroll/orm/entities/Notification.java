package com.springroll.orm.entities;


import com.springroll.core.AckLog;
import com.springroll.core.services.notification.INotification;
import com.springroll.core.services.notification.INotificationMessage;
import com.springroll.orm.AckLogConverter;
import com.springroll.orm.CSVSetConverter;
import com.springroll.orm.JavaSerializationConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Version;
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

    @Column(name = "PAYLOAD", columnDefinition = "BLOB")
    @Convert(converter = JavaSerializationConverter.class)
    private INotificationMessage notificationMessage;

    @Column(name = "RECEIVERS")
    private String receivers;

    @Column(name = "USERS")
    @Convert(converter = CSVSetConverter.class)
    private Set<String> users;

    @Column(name = "CHANNEL_NAME")
    private String channelName;

    @Column(name = "ACK_LOG")
    @Convert(converter = AckLogConverter.class)
    private List<AckLog> ackLog = new ArrayList<>();

    @Column(name = "CREATION_TIME")
    private LocalDateTime creationTime;

    @Column(name = "INITIATOR")
    private String initiator;

    @Column(name = "AUTO_CLEAN")
    private Boolean autoClean;

    @Version
    @Column(name = "VERSION")
    private Long version;


    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }

    public INotificationMessage getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(INotificationMessage notificationMessage) {
        this.notificationMessage = notificationMessage;
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

    public void addAck(AckLog ackLog){
        this.ackLog.add(ackLog);
    }

    public List<AckLog> getAckLog() {
        return ackLog;
    }

    public void setAckLog(List<AckLog> ackLog) {
        this.ackLog = ackLog;
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