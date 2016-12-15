package com.springroll.orm;

import com.springroll.core.CSVList;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by anishjoseph on 31/10/16.
 */
@Converter(autoApply = true)
public class CSVListConverter implements AttributeConverter<CSVList, String> {
    @Override
    public String convertToDatabaseColumn(CSVList value) {
        String s = String.join(CSVList.getSeparator(), value);
        return s;
    }

    @Override
    public CSVList convertToEntityAttribute(String value) {
        CSVList<String> csvList = new CSVList<>();
        String[] split = value.split(CSVList.getSeparator());
        for (String s : split) {
            csvList.add(s);
        }
        return csvList;
    }
}
