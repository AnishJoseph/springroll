package com.springroll.notification;

import com.springroll.core.notification.INotificationMessage;
import com.springroll.core.notification.IReviewMeta;
import org.springframework.stereotype.Component;

/**
 * Created by anishjoseph on 05/10/16.
 */
@Component public class FyiNotificationMessageFactory extends AbstractNotificationMessageFactory {

    @Override
    public INotificationMessage makeMessage(IReviewMeta notificationMeta) {
        return null;
    }
}