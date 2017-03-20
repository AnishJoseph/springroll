package com.springroll.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Created by anishjoseph on 05/12/16.
 */
@Service
public class SpringrollUtils {
    @Autowired private DefaultConversionService conversionService;
    private DateTimeFormatter dateFormatter;
    private DateTimeFormatter dateTimeFormatter;

    @Value("${datetime.format.java}")
    private String dateTimeFormat = "dd-MM-yyyy HH:mm";

    @Value("${date.format.java}")
    private String dateFormat = "dd-MM-yyyy";

    /**
     * Helper method to convert (typically a String) into an appropriate type
     * @param paramValue - the value
     * @param parameterType - the java class to convert to
     * @return the converted object
     */
    public Object convert(Object paramValue, Class parameterType){
        if(parameterType.equals(LocalDateTime.class)){
            return Instant.ofEpochMilli((long) paramValue).atZone(ZoneId.systemDefault()).toLocalDateTime();
        } else if (parameterType.equals(LocalDate.class)) {
            return Instant.ofEpochMilli((long) paramValue).atZone(ZoneId.systemDefault()).toLocalDate();
        }
        Object o = conversionService.convert(paramValue, parameterType);
        return o;
    }

    public DecimalFormat makeFormatter(Locale locale, String pattern){
        return new DecimalFormat(pattern, new DecimalFormatSymbols(locale));
    }

    public DateTimeFormatter getDateTimeFormatter(){
        if(dateTimeFormatter == null){
            dateTimeFormatter = DateTimeFormatter.ofPattern(dateTimeFormat);
        }
        return dateTimeFormatter;
    }
    public DateTimeFormatter getDateFormatter(){
        if(dateFormatter == null){
            dateFormatter = DateTimeFormatter.ofPattern(dateFormat);
        }
        return dateFormatter;
    }

}
