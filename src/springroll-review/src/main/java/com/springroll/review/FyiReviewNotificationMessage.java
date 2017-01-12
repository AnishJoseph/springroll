package com.springroll.review;

import com.springroll.core.LocaleFactory;
import com.springroll.core.SpringrollSecurity;
import com.springroll.core.services.notification.DismissibleNotificationMessage;
import com.springroll.core.services.notification.IReviewMeta;
import com.springroll.core.services.notification.AlertType;

/**
 * Created by anishjoseph on 02/10/16.
 */
public class FyiReviewNotificationMessage extends AbstractReviewNotificationMessage implements DismissibleNotificationMessage {
    {
        alertType = AlertType.INFO;
    }
    public  FyiReviewNotificationMessage(){
    }
    public FyiReviewNotificationMessage(IReviewMeta reviewMeta, String messageKey, String[] args) {
        super(reviewMeta, messageKey, args);
    }
    @Override
    public String getMessage() {
        return LocaleFactory.getLocalizedServerMessage(SpringrollSecurity.getUser().getLocale(), getMessageKey(), getArgs());
    }

}
