package com.springroll.core;

import java.util.List;

/**
 * All enrichers must implement this interface. As events pass through the synchronous route
 * the enricher for the event is invoked. The method that implements this interface can modify
 * the DTOs passed to it.
 * The mapping of a DTO to its enricher is stored when the add method of JobDefinitions is invoked
 * and must be done at initialization.
 *
 * Created by Anish Joseph on 12/09/16.
 * @since 1.0
 */
public interface DTOEnricher {
    void enrich(List<? extends DTO> dtos);
}
