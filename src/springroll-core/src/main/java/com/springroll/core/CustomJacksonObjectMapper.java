package com.springroll.core;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by anishjoseph on 06/01/17.
 */
public class CustomJacksonObjectMapper extends ObjectMapper {
    private static final long serialVersionUID = -2390464962347652787L;

    public CustomJacksonObjectMapper(){
        super();
        this.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
        this.registerModule(new DateTimeModule());
    }
}
