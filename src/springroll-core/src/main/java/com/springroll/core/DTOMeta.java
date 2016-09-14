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
    public void addDTOType(Class<? extends DTO> clazz, Class<? extends IEvent> eventClass, DTOEnricher enricher){
        dtoMeta.put(clazz, new DTOType(enricher, eventClass));
    }

    public Class<? extends IEvent> getEventForDTO(DTO dto) {
        DTOType dtoType = dtoMeta.get(dto.getClass());
        return dtoType.eventClass;
    }
    public DTOEnricher getEnricherForDTO(DTO dto) {
        DTOType dtoType = dtoMeta.get(dto.getClass());
        return dtoType.enricher;
    }

    private class DTOType {
        private DTOEnricher enricher;
        private Class<? extends IEvent> eventClass;

        public DTOType(DTOEnricher enricher, Class<? extends IEvent> eventClass) {
            this.enricher = enricher;
            this.eventClass = eventClass;
        }
        public DTOType( Class<? extends IEvent> eventClass) {
            this.eventClass = eventClass;
        }

    }
}
