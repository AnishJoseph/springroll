package com.springroll.review;

import com.springroll.core.SpringrollSecurity;
import com.springroll.core.notification.INotificationMessage;
import com.springroll.core.notification.IReviewMeta;
import com.springroll.core.services.IReviewNotificationMessageFactory;
import com.springroll.notification.AbstractReviewNotificationMessageFactory;
import org.springframework.stereotype.Component;

/**
 * Created by anishjoseph on 05/10/16.
 */
@Component public class ReviewNotificationMessageFactory extends AbstractReviewNotificationMessageFactory implements IReviewNotificationMessageFactory {

    @Override public INotificationMessage makeMessage(IReviewMeta notificationMeta){
        String msgKey = notificationMeta.getApprover().equals(SpringrollSecurity.getUser().getUsername()) ? "ui.review.noti.self.msg" : "ui.review.noti.msg";
        return new ReviewNotificationMessage(notificationMeta.getReviewStepIds(), notificationMeta.getApprover(), notificationMeta.getBusinessValidationResults(), msgKey, new String[]{notificationMeta.getService(), notificationMeta.getInitiator().getUsername()}, notificationMeta.getReviewLogs(), notificationMeta.getInitiator().getUsername());
    }
}
