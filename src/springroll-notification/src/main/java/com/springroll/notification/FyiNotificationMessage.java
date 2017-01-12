package com.springroll.notification;

import com.springroll.core.LocaleFactory;
import com.springroll.core.SpringrollSecurity;
import com.springroll.core.services.notification.DismissibleNotificationMessage;
import com.springroll.core.services.notification.AlertType;

/**
 * Created by anishjoseph on 05/10/16.
 */
public class FyiNotificationMessage extends AbstractAlertNotificationMessage implements DismissibleNotificationMessage {
    private String messageKey;
    private String[] args = new String[]{};

    {
        alertType = AlertType.INFO;
    }

    public FyiNotificationMessage() {
    }

    public FyiNotificationMessage(String messageKey, String[] args, String notificationReceivers) {
        this.messageKey = messageKey;
        this.args = args;
        setNotificationReceivers(notificationReceivers);
        setInitiator(SpringrollSecurity.getUser().getUsername());
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    @Override
    public String getMessage() {
        return LocaleFactory.getLocalizedServerMessage(SpringrollSecurity.getUser().getLocale(), messageKey, args);
    }
}
