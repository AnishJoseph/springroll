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
    public static void add(Class<? extends DTO> clazz, Class<? extends IEvent> eventClass, DTOEnricher enricher, DTOBusinessValidator businessValidator){
        dtoMeta.put(clazz, new JobDefinition(eventClass, businessValidator, enricher));
    }

    public static Class<? extends IEvent> getEventForDTO(DTO dto) {
        JobDefinition jobDefinition = dtoMeta.get(dto.getClass());
        return jobDefinition.eventClass;
    }
    public static DTOEnricher getEnricherForDTO(DTO dto) {
        JobDefinition jobDefinition = dtoMeta.get(dto.getClass());
        return jobDefinition.enricher;
    }

    public static DTOBusinessValidator getBusinessValidatorForDTO(DTO dto) {
        JobDefinition jobDefinition = dtoMeta.get(dto.getClass());
        return jobDefinition.businessValidator;
    }

    private static class JobDefinition{
        private DTOEnricher enricher;
        private DTOBusinessValidator businessValidator;
        private Class<? extends IEvent> eventClass;

        public JobDefinition(Class<? extends IEvent> eventClass, DTOBusinessValidator businessValidator, DTOEnricher enricher) {
            this.eventClass = eventClass;
            this.businessValidator = businessValidator;
            this.enricher = enricher;
        }

        public JobDefinition(Class<? extends IEvent> eventClass, DTOBusinessValidator businessValidator) {
            this.eventClass = eventClass;
            this.businessValidator = businessValidator;
        }

        public JobDefinition(Class<? extends IEvent> eventClass, DTOEnricher enricher) {
            this.eventClass = eventClass;
            this.enricher = enricher;
        }

        public JobDefinition(Class<? extends IEvent> eventClass) {
            this.eventClass = eventClass;
        }
    }

}
