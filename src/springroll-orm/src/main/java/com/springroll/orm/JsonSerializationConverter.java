package com.springroll.orm;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.springroll.core.ReviewLog;
import org.hibernate.internal.util.SerializationHelper;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * Created by anishjoseph on 31/10/16.
 */
public abstract class JsonSerializationConverter implements AttributeConverter<Object, String> {
    protected abstract TypeReference getTypeReference();

    @Override
    public String convertToDatabaseColumn(Object value) {
        if(value == null)return null;
        try {
            return getObjectMapper().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object convertToEntityAttribute(String value) {
        if(value == null)return null;
        try {
            return getObjectMapper().readValue(value, getTypeReference());
        } catch (IOException e) {
            //FIXME
            throw new RuntimeException(e);
        }
    }

    private ObjectMapper getObjectMapper(){
        return  new ObjectMapper().registerModule(new JavaTimeModule());
    }
}
