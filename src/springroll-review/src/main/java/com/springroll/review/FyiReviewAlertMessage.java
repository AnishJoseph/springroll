package com.springroll.review;

import com.springroll.core.LocaleFactory;
import com.springroll.core.SpringrollSecurity;
import com.springroll.core.services.notification.DismissibleAlertMessage;
import com.springroll.core.services.notification.IReviewMeta;
import com.springroll.core.services.notification.AlertType;

/**
 * Created by anishjoseph on 02/10/16.
 */
public class FyiReviewAlertMessage extends AbstractReviewAlertMessage implements DismissibleAlertMessage {
    {
        alertType = AlertType.INFO;
    }
    public FyiReviewAlertMessage(){
    }
    public FyiReviewAlertMessage(IReviewMeta reviewMeta, String messageKey, String[] args) {
        super(reviewMeta, messageKey, args);
    }
    @Override
    public String getMessage() {
        return LocaleFactory.getLocalizedMessage(SpringrollSecurity.getUser().getLocale(), getMessageKey(), getArgs());
    }

}
