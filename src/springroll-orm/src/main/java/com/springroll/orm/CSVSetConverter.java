package com.springroll.orm;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by anishjoseph on 31/10/16.
 */
@Converter(autoApply = true)
public class CSVSetConverter implements AttributeConverter<Set, String> {
    private static final String separator = ",";
    @Override
    public String convertToDatabaseColumn(Set value) {
        return value == null ? null :  String.join(separator, value);
    }

    @Override
    public Set convertToEntityAttribute(String value) {
        return value == null ? null : Stream.of(value.split(separator)).collect(Collectors.toSet());
    }
}
