package com.springroll.core.notification;

import com.springroll.core.BusinessValidationResult;
import com.springroll.core.ReviewLog;
import com.springroll.core.SpringrollUser;

import java.util.List;
import java.util.Set;

/**
 * Created by anishjoseph on 05/10/16.
 */
public interface INotificationMessageFactory {
    Set<String> getTargetUsers(INotificationMessage notificationMessage);
    List<? extends INotification> getPendingNotificationsForUser(INotificationChannel notificationChannel);
    //FIXME - no reason why we should have a make message - its required only for alerts??
    INotificationMessage makeMessage(List<Long> reviewStepIds, String approver, List<BusinessValidationResult> businessValidationResults, SpringrollUser initiator, String service, List<ReviewLog> reviewLog);
}
