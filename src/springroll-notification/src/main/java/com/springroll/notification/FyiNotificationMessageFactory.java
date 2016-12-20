package com.springroll.notification;

import com.springroll.core.notification.INotificationMessage;
import com.springroll.core.notification.IReviewMeta;
import com.springroll.orm.repositories.Repositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by anishjoseph on 05/10/16.
 */
@Component public class FyiNotificationMessageFactory extends AbstractNotificationMessageFactory {

    @Autowired Repositories repositories;

    @Override
    public INotificationMessage makeMessage(IReviewMeta notificationMeta) {
        return null;
    }
}