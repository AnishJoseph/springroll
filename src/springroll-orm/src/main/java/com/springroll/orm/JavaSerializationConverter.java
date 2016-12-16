package com.springroll.orm;

import org.hibernate.internal.util.SerializationHelper;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.Serializable;

/**
 * Created by anishjoseph on 31/10/16.
 */
@Converter(autoApply = false)
public class JavaSerializationConverter implements AttributeConverter<Serializable, byte[]> {
    @Override
    public byte[] convertToDatabaseColumn(Serializable value) {
        return value == null ? null : SerializationHelper.serialize(value);
    }

    @Override
    public Serializable convertToEntityAttribute(byte[] value) {
        return value == null ? null : (Serializable) SerializationHelper.deserialize(value);
    }
}
