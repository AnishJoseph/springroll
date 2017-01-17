package com.springroll.core;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Created by anishjoseph on 06/01/17.
 */
public class ReviewLogSerializer extends StdScalarSerializer<ReviewLog> {

    public ReviewLogSerializer() {
        super(ReviewLog.class);
    }

    @Override
    public void serialize(ReviewLog reviewLog,JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException, JsonGenerationException {
        jsonGenerator.writeString(reviewLog.getReviewer() + ":" + (reviewLog.isApproved() ? "Yes" : "No"));
    }
}