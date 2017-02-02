package com.springroll.review;

import com.springroll.core.services.notification.IAlertMessage;
import com.springroll.core.services.notification.IReviewMeta;
import com.springroll.core.services.review.IFyiReviewNotificationMessageFactory;
import com.springroll.notification.AbstractReviewNotificationMessageFactory;
import org.springframework.stereotype.Component;

/**
 * Created by anishjoseph on 05/10/16.
 */
@Component public class FyiReviewAlertFactory extends AbstractReviewNotificationMessageFactory implements IFyiReviewNotificationMessageFactory {
    @Override public IAlertMessage makeMessage(IReviewMeta reviewMeta){
        return new FyiReviewAlertMessage(reviewMeta, "ui.fyi.review.noti.msg", new String[]{reviewMeta.getService(), reviewMeta.getInitiator()});
    }
}
