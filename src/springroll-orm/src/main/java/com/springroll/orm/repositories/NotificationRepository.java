package com.springroll.orm.repositories;

import com.springroll.orm.entities.Notification;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

/**
 * Created by anishjoseph on 12/09/16.
 */
public interface NotificationRepository extends AbstractEntityRepository<Notification>{

    List<Notification> findByChannelNameAndReceiversIn(String channelName, Collection<String> roles);

    List<Notification> findByChannelNameAndInitiatorNotAndReceiversIn(String channelName, String userId, Collection<String> roles);
    List<Notification> findByChannelNameAndReceivers(String channelName, String userId);

}
