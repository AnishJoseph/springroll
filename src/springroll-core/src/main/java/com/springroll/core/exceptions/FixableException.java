package com.springroll.core.exceptions;

/**
 * Created by anishjoseph on 21/10/16.
 */
public class FixableException extends RuntimeException {

    private String[] messageArguments;
    private String messageKey;
    public FixableException(){}
    public FixableException(String message, String messageKey, String... messageArguments){
        super(message);
        this.messageArguments = messageArguments;
        this.messageKey = messageKey;
    }
    public FixableException(String message, String messageKey){
        super(message);
        this.messageKey = messageKey;
    }
    public FixableException(String message){
        super(message);
    }

    public String[] getMessageArguments() {
        return messageArguments;
    }

    public void setMessageArguments(String[] messageArguments) {
        this.messageArguments = messageArguments;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }
}
