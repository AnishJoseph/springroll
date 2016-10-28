package com.springroll.core.notification;

import com.springroll.core.BusinessValidationResult;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Set;

/**
 * Created by anishjoseph on 05/10/16.
 */
public interface INotificationMessageFactory {
    Set<String> getTargetUsers(INotificationMessage notificationMessage);
    List<? extends INotification> getPendingNotificationsForUser(INotificationChannel notificationChannel);
    INotificationMessage makeMessage(List<Long> reviewStepIds, String approver, List<BusinessValidationResult> businessValidationResults, User initiator);
}
