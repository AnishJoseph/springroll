package com.springroll.orm.repositories;

import com.springroll.orm.entities.Notification;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

/**
 * Created by anishjoseph on 12/09/16.
 */
public interface NotificationRepository extends AbstractEntityRepository<Notification>{

    @Query("select notification from Notification notification where notification.channelName = ?1 and (notification.receivers = ?2 or notification.receivers in (?3))")
    List<Notification> findNotificationsForUser(String channelName, String userId, Collection<String> group);

}
