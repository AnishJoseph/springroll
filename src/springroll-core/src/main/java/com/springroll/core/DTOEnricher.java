package com.springroll.core;

import java.util.List;

/**
 * Created by anishjoseph on 12/09/16.
 */
public interface DTOEnricher {
    void enrich(List<? extends DTO> dtos);
}
