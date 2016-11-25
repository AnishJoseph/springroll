package com.springroll.review;

import com.springroll.core.notification.INotificationMessage;
import com.springroll.core.notification.INotificationMeta;
import com.springroll.core.services.IReviewNotificationMessageFactory;
import com.springroll.notification.AbstractNotificationMessageFactory;
import org.springframework.stereotype.Component;

/**
 * Created by anishjoseph on 05/10/16.
 */
@Component public class ReviewNotificationMessageFactory extends AbstractNotificationMessageFactory implements IReviewNotificationMessageFactory {
    @Override public INotificationMessage makeMessage(INotificationMeta notificationMeta){
        return new ReviewNotificationMessage(notificationMeta.getReviewStepIds(), notificationMeta.getApprover(), notificationMeta.getBusinessValidationResults(), "ui.review.noti.msg", new String[]{notificationMeta.getService(), notificationMeta.getInitiator().getUsername()}, notificationMeta.getReviewLogs());
    }
}
