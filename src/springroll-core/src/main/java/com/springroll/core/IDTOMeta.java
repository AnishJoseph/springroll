package com.springroll.core;

/**
 * Created by anishjoseph on 12/09/16.
 */
public interface IDTOMeta {
    Class<? extends IEvent> getEventForDTO(DTO dto);
}
