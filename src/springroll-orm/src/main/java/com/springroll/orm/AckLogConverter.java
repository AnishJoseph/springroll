package com.springroll.orm;

import com.fasterxml.jackson.core.type.TypeReference;
import com.springroll.core.AckLog;
import com.springroll.core.ReviewLog;

import javax.persistence.Converter;
import java.util.List;

/**
 * Created by anishjoseph on 15/12/16.
 */
@Converter(autoApply = false)
public class AckLogConverter extends JsonSerializationConverter {
    @Override
    protected TypeReference getTypeReference() {
        return new TypeReference<List<AckLog>>(){};
    }
}
