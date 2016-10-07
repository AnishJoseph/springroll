package com.springroll.core;

import java.io.Serializable;

/**
 * Created by anishjoseph on 27/09/16.
 */
public class BusinessValidationResult implements Serializable{
    private int dtoIndex;
    private String field;
    private String messageKey;
    private String[] args;
    private String violatedRule;
    private String approver;

    public BusinessValidationResult() {
    }

    public BusinessValidationResult(int dtoIndex, String field, String messageKey, String[] args, String violatedRule, String approver) {
        this.dtoIndex = dtoIndex;
        this.field = field;
        this.messageKey = messageKey;
        this.violatedRule = violatedRule;
        this.approver = approver;
        this.args = args;
    }

    public String getField() {
        return field;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public String[] getArgs() {
        return args;
    }

    public String getViolatedRule() {
        return violatedRule;
    }

    public int getDtoIndex() {
        return dtoIndex;
    }

    public String getApprover() {
        return approver;
    }
}
