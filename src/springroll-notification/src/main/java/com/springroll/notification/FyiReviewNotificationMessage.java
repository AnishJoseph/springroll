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
public class FyiReviewNotificationMessage extends AbstractNotificationMessage{
    transient private List<BusinessValidationResult> businessValidationResult;
    private String  businessValidationResultJson = "";
    private String message;

    public FyiReviewNotificationMessage(){}

    public FyiReviewNotificationMessage(String approver, List<BusinessValidationResult> businessValidationResult, String message) {
        setNotificationReceivers(approver);
        setBusinessValidationResult(businessValidationResult);
        this.message = message;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
