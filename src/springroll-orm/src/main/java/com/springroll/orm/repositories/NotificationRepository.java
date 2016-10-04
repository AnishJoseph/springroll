package com.springroll.orm.repositories;

import com.springroll.orm.entities.Notification;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

/**
 * Created by anishjoseph on 12/09/16.
 */
public interface NotificationRepository extends AbstractEntityRepository<Notification>{

    @Query("select notification from Notification notification where notification.notificationChannelName = ?1 and (notification.notificationReceivers = ?2 or notification.notificationReceivers in (?3))")
    List<Notification> findNotificationsForUser(String channelName, String userId, Collection<String> group);

}
