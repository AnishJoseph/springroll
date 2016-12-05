package com.springroll.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Created by anishjoseph on 05/12/16.
 */
@Service
public class SpringrollUtils {
    @Autowired private DefaultConversionService conversionService;

    /**
     * Helper method to convert (typically a String) into an appropriate type
     * @param paramValue - the value
     * @param parameterType - the java class to convert to
     * @return the converted object
     */
    public Object convert(Object paramValue, Class parameterType){
        if(parameterType.equals(LocalDateTime.class)){
            return LocalDateTime.parse((String) paramValue, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")); //FIXME - externalize pattern
        } else if (parameterType.equals(LocalDate.class)) {
            return LocalDate.parse((String) paramValue, DateTimeFormatter.ofPattern("dd/MM/yyyy")); //FIXME - externalize pattern
        }
        Object o = conversionService.convert(paramValue, parameterType);
        return o;
    }

    public DecimalFormat makeFormatter(Locale locale, String pattern){
        return new DecimalFormat(pattern, new DecimalFormatSymbols(locale));
    }
}
