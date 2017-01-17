package com.springroll.core;

import com.fasterxml.jackson.databind.module.SimpleModule;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by anishjoseph on 06/01/17.
 */
public class CustomSpringrollJSONSerializers extends SimpleModule {

    private static final long serialVersionUID = 8255435304076759863L;

    public CustomSpringrollJSONSerializers() {
        super();
        addSerializer(LocalDateTime.class, new DateTimeSerializer());
        addSerializer(LocalDate.class, new DateSerializer());
    }
}