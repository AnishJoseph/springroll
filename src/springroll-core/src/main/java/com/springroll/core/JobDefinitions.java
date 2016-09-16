package com.springroll.core;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anish Joseph on 12/09/16.
 * @since 1.0
 */
public class JobDefinitions{
    private static Map<Class, JobDefinition> dtoMeta = new HashMap<>();

    public static void add(Class<? extends DTO> clazz, Class<? extends IEvent> eventClass){
        dtoMeta.put(clazz, new JobDefinition(eventClass));
    }
    public static void add(Class<? extends DTO> clazz, Class<? extends IEvent> eventClass, DTOEnricher enricher){
        dtoMeta.put(clazz, new JobDefinition(enricher, eventClass));
    }

    public static Class<? extends IEvent> getEventForDTO(DTO dto) {
        JobDefinition jobDefinition = dtoMeta.get(dto.getClass());
        return jobDefinition.eventClass;
    }
    public static DTOEnricher getEnricherForDTO(DTO dto) {
        JobDefinition jobDefinition = dtoMeta.get(dto.getClass());
        return jobDefinition.enricher;
    }

    private static class JobDefinition{
        private DTOEnricher enricher;
        private Class<? extends IEvent> eventClass;

        public JobDefinition(DTOEnricher enricher, Class<? extends IEvent> eventClass) {
            this.enricher = enricher;
            this.eventClass = eventClass;
        }
        public JobDefinition( Class<? extends IEvent> eventClass) {
            this.eventClass = eventClass;
        }
    }

}
