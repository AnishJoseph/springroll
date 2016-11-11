package com.springroll.core.exceptions;

import com.springroll.core.LocaleFactory;

import java.util.Locale;

/**
 * Created by anishjoseph on 21/10/16.
 */
public class SpringrollException extends RuntimeException {

    private String[] messageArguments;
    private String messageKey;
    public SpringrollException(){}
    public SpringrollException(String messageKey, String... messageArguments){
        super(LocaleFactory.getLocalizedServerMessage(Locale.getDefault(), messageKey, (Object[])messageArguments));
        this.messageArguments = messageArguments;
        this.messageKey = messageKey;
    }

    public String[] getMessageArguments() {
        return messageArguments;
    }

    public String getMessageKey() {
        return messageKey;
    }
}
