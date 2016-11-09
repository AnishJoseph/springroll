package com.springroll.notification;

import com.springroll.core.BusinessValidationResult;
import com.springroll.core.SpringrollUser;
import com.springroll.core.notification.INotificationMessage;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by anishjoseph on 05/10/16.
 */
@Component public class MdmReviewNotificationMessageFactory extends AbstractNotificationMessageFactory {

    @Override public INotificationMessage makeMessage(List<Long> reviewStepIds, String approver, List<BusinessValidationResult> businessValidationResults, SpringrollUser initiator, String serviceName){
        return new MdmReviewNotificationMessage(reviewStepIds, approver, "ui.mdm.review.noti.msg", businessValidationResults.get(0).getArgs());
    }
}
