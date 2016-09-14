package com.springroll.core;

/**
 * Created by anishjoseph on 12/09/16.
 */
public interface DTOEnricher {
    void enrich(DTO dto, Principal principal);
}
