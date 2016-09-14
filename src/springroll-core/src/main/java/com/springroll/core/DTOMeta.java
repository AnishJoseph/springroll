package com.springroll.core;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anishjoseph on 12/09/16.
 */
@Component
public class DTOMeta implements IDTOMeta{
    Map<Class, DTOType> dtoMeta = new HashMap<Class, DTOType>();

    public void addDTOType(Class<? extends DTO> clazz, Class<? extends IEvent> eventClass){
        dtoMeta.put(clazz, new DTOType(eventClass));
    }

    public Class<? extends IEvent> getEventForDTO(DTO dto) {
        DTOType dtoType = dtoMeta.get(dto.getClass());
        return dtoType.eventClass;
    }

    private class DTOType {
        private Class<DTOEnricher> enricherClass;
        private Class<? extends IEvent> eventClass;

        public DTOType(Class<DTOEnricher> enricherClass, Class<AbstractEvent> eventClass) {
            this.enricherClass = enricherClass;
            this.eventClass = eventClass;
        }
        public DTOType( Class<? extends IEvent> eventClass) {
            this.eventClass = eventClass;
        }

    }
}
