package com.springroll.core;

/**
 * Created by anishjoseph on 07/10/16.
 */
public interface IDTOProcessors {
    Class<? extends DTOEnricher> getEnricherClass();
    Class<? extends DTOBusinessValidator> getBusinessValidatorClass();
    Class<? extends IEvent> getEventClass();
}
