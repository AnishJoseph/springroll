package com.springroll.review;

import com.springroll.core.BusinessValidationResult;
import com.springroll.core.ReviewLog;
import com.springroll.core.SpringrollUser;
import com.springroll.core.notification.*;
import com.springroll.core.services.IReviewNotificationMessageFactory;
import com.springroll.notification.AbstractNotificationMessageFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by anishjoseph on 05/10/16.
 */
@Component public class ReviewNotificationMessageFactory extends AbstractNotificationMessageFactory implements IReviewNotificationMessageFactory {
    @Override public INotificationMessage makeMessage(List<Long> reviewStepIds, String approver, List<BusinessValidationResult> businessValidationResults, SpringrollUser initiator, String serviceName, List<ReviewLog> reviewLog){
        return new ReviewNotificationMessage(reviewStepIds, approver, businessValidationResults, "ui.review.noti.msg", new String[]{serviceName, initiator.getUsername()}, reviewLog);
    }
}
