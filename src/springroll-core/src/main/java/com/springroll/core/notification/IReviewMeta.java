package com.springroll.core.notification;

import com.springroll.core.BusinessValidationResult;
import com.springroll.core.DTO;
import com.springroll.core.ReviewLog;
import com.springroll.core.SpringrollUser;

import java.util.List;

/**
 * Created by anishjoseph on 25/11/16.
 * The Meta data related to a review - currently passed to the makeMessage method of the INotificationMessageFactory
 */

public interface IReviewMeta {
    List<Long> getReviewStepIds(); /* The ids of the review steps currently under review */
    String getApprover(); /* The approver for these review step(s) - can be a role or a user */
    List<BusinessValidationResult> getBusinessValidationResults(); /* The business rules that were beached that has caused this review */
    SpringrollUser getInitiator(); /* The initiator of the transaction */
    String getService();            /* The service */
    List<ReviewLog> getReviewLogs(); /* Logs of previous reviews of business rules breached for this transaction */
    List<DTO> getDtosUnderReview();  /* The DTOs of the transaction */
}
