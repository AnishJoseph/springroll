package com.springroll.orm;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.List;

/**
 * Created by anishjoseph on 31/10/16.
 */
@Converter(autoApply = true)
public class CSVListConverter implements AttributeConverter<List, String> {
    private static final String separator = ",";
    @Override
    public String convertToDatabaseColumn(List value) {
        return value == null ? null :  String.join(separator, value);
    }

    @Override
    public List convertToEntityAttribute(String value) {
        return value == null ? null : Arrays.asList(value.split(separator));
    }
}
