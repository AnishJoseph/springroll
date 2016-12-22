package com.springroll.core.services.notification;

/**
 * Created by anishjoseph on 05/10/16.
 */
public interface IReviewableNotificationMessageFactory extends INotificationMessageFactory{
    INotificationMessage makeMessage(IReviewMeta notificationMeta);
}
