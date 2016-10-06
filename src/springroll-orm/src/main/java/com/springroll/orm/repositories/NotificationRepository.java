package com.springroll.orm.repositories;

import com.springroll.orm.entities.Notification;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

/**
 * Created by anishjoseph on 12/09/16.
 */
public interface NotificationRepository extends AbstractEntityRepository<Notification>{

    List<Notification> findByChannelNameAndAckLogAsJsonNotLikeAndReceiversIn(String channelName, String quotedUserId, Collection<String> roles);

    List<Notification> findByChannelNameAndInitiatorNotLikeAndAckLogAsJsonNotLikeAndReceiversIn(String channelName, String userId, String quotedUserId, Collection<String> roles);

}
