package com.springroll.core;

/**
 * Created by anishjoseph on 27/09/16.
 */
public interface IBusinessValidationResults {
    //FIXME - add @Nonnull and other annotations to help validation - even if just IDE
    //FIXME - documentation

    /**
     * When any of the DTOs has a business violation the routing is stopped and a BusinessValidationException is thrown.
     * The payload of the exception contains the list of business violations encountered. Note the routing is not stopped
     * immediately - all DTOs in the payload will be validated before the exception is thrown.
     *
     * @param dtoIndex - the index of the DTO which had the violation
     * @param field - the field, if any, that caused the violation. Unlike property validations, a business violation may not be directly linked to a field.
     * @param messageKey - the message key that will be used to generate the error message
     * @param args - arguments to the message key that will be used construct the error message
     */
    void addBusinessViolation(int dtoIndex, String field, String messageKey, String[] args);

    void addReviewNeeded(int dtoIndex, String field, String messageKey, String[] args, String violatedRule);
    /**
     * Use this to send a notification to a set of people - this is not for review but just as informational
     * The payload of the exception contains the list of such notifications needed
     *
     * @param dtoIndex - the index of the DTO which had the violation
     * @param field - the field, if any, that caused the violation. Unlike property validations, this violation may not be directly linked to a field.
     * @param messageKey - the message key that will be used to generate the error message
     * @param args - arguments to the message key that will be used construct the error message
     * @param violatedRule - the rule name in table ReviewRule that was breached
     * @param approver - the role to which the notification needs to be sent to - overrides the role given in table ReviewRule
     *
     */
    void addReviewNeeded(int dtoIndex, String field, String messageKey, String[] args, String violatedRule, String approver);
}
