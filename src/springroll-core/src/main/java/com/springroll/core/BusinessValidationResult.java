package com.springroll.core;

/**
 * Created by anishjoseph on 27/09/16.
 */
public class BusinessValidationResult {
    private String field;
    private String messageKey;
    private String[] args;
    private String violatedRule;
    private String userOrGroup;

    public BusinessValidationResult(String violatedRule, String userOrGroup) {
        this.violatedRule = violatedRule;
        this.userOrGroup = userOrGroup;
    }
    public BusinessValidationResult(String violatedRule) {
        this.violatedRule = violatedRule;
    }

    public BusinessValidationResult(String field, String messageKey, String[] args, String violatedRule) {
        this.field = field;
        this.messageKey = messageKey;
        this.args = args;
        this.violatedRule = violatedRule;
    }

    public BusinessValidationResult(String field, String messageKey, String[] args, String violatedRule, String userOrGroup) {
        this.field = field;
        this.messageKey = messageKey;
        this.args = args;
        this.violatedRule = violatedRule;
        this.userOrGroup = userOrGroup;
    }

    public BusinessValidationResult(String field, String messageKey, String[] args) {
        this.field = field;
        this.messageKey = messageKey;
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
}
