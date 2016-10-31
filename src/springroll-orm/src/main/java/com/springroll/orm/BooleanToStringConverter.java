package com.springroll.orm;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by anishjoseph on 31/10/16.
 */
@Converter(autoApply = true)
public class BooleanToStringConverter implements AttributeConverter<Boolean, String> {
    @Override
    public String convertToDatabaseColumn(Boolean value) {
        return (value != null && value) ? "T" : "F";
    }

    @Override
    public Boolean convertToEntityAttribute(String value) {
        return "T".equals(value);
    }
}
