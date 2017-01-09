package com.springroll.core;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Created by anishjoseph on 06/01/17.
 */
public class DateTimeSerializer extends StdScalarSerializer<LocalDateTime> {

    public DateTimeSerializer() {
        super(LocalDateTime.class);
    }

    @Override
    public void serialize(LocalDateTime dateTime,JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException, JsonGenerationException {
        jsonGenerator.writeNumber(dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
    }
}