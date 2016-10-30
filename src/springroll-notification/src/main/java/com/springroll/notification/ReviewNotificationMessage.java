package com.springroll.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.springroll.core.BusinessValidationResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anishjoseph on 02/10/16.
 */
public class ReviewNotificationMessage extends FyiReviewNotificationMessage{
    private List<Long> reviewStepId;

    public ReviewNotificationMessage(){}

    public ReviewNotificationMessage(List<Long> reviewStepId, String approver, List<BusinessValidationResult> businessValidationResult, String messageKey, String[] args) {
        super(approver, businessValidationResult, messageKey, args);
        this.reviewStepId = reviewStepId;
    }

    public List<Long> getReviewStepId() {
        return reviewStepId;
    }

    public void setReviewStepId(List<Long> reviewStepId) {
        this.reviewStepId = reviewStepId;
    }
}
