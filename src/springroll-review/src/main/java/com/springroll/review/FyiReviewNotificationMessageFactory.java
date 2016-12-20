package com.springroll.review;

import com.springroll.core.notification.INotificationMessage;
import com.springroll.core.notification.IReviewMeta;
import com.springroll.core.services.IFyiReviewNotificationMessageFactory;
import com.springroll.notification.AbstractReviewNotificationMessageFactory;
import org.springframework.stereotype.Component;

/**
 * Created by anishjoseph on 05/10/16.
 */
@Component public class FyiReviewNotificationMessageFactory extends AbstractReviewNotificationMessageFactory implements IFyiReviewNotificationMessageFactory {
    @Override public INotificationMessage makeMessage(IReviewMeta notificationMeta){
        return new FyiReviewNotificationMessage(notificationMeta.getApprover(), notificationMeta.getBusinessValidationResults(), "ui.fyi.review.noti.msg", new String[]{notificationMeta.getApprover(), notificationMeta.getInitiator().getUsername()}, notificationMeta.getReviewLogs(), notificationMeta.getInitiator().getUsername());
    }
}
