package com.springroll.core.notification;

import com.springroll.core.BusinessValidationResult;
import com.springroll.core.DTO;
import com.springroll.core.ReviewLog;
import com.springroll.core.SpringrollUser;

import java.util.List;

/**
 * Created by anishjoseph on 25/11/16.
 */
public interface INotificationMeta {
    List<Long> getReviewStepIds();
    String getApprover();
    List<BusinessValidationResult> getBusinessValidationResults();
    SpringrollUser getInitiator();
    String getService();
    List<ReviewLog> getReviewLogs();
    List<DTO> getDtosUnderReview();
}
