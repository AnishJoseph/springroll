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
public class ReviewNotificationMessage extends AbstractNotificationMessage{
    private List<Long> reviewStepId;
    transient private List<BusinessValidationResult> businessValidationResult;
    private String  businessValidationResultJson = "";

    public ReviewNotificationMessage(){}

    public ReviewNotificationMessage(List<Long> reviewStepId, String approver, List<BusinessValidationResult> businessValidationResult) {
        this.reviewStepId = reviewStepId;
        setNotificationReceivers(approver);
        setBusinessValidationResult(businessValidationResult);
    }

    public List<Long> getReviewStepId() {
        return reviewStepId;
    }

    public void setReviewStepId(List<Long> reviewStepId) {
        this.reviewStepId = reviewStepId;
    }

    public List<BusinessValidationResult> getBusinessValidationResult() {
        if (this.businessValidationResultJson.isEmpty() )return new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            businessValidationResult = mapper.readValue(businessValidationResultJson,  new TypeReference<List<BusinessValidationResult>>(){});
            return businessValidationResult;
        } catch (IOException e) {
            //FIXME
            throw new RuntimeException(e);
        }
    }

    public void setBusinessValidationResult(List<BusinessValidationResult> businessValidationResult) {
        this.businessValidationResult = businessValidationResult;
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            businessValidationResultJson = mapper.writeValueAsString(businessValidationResult);
        } catch (JsonProcessingException e) {
            //FIXME
            throw new RuntimeException(e);
        }
    }

    public String getBusinessValidationResultJson() {
        return businessValidationResultJson;
    }

    public void setBusinessValidationResultJson(String businessValidationResultJson) {
        this.businessValidationResultJson = businessValidationResultJson;
    }
}
