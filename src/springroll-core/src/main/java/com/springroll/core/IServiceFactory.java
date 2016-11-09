package com.springroll.core;

/**
 * Created by anishjoseph on 09/11/16.
 */
public interface IServiceFactory {
    DTOEnricher getServiceEnricher();  //FIXME change to IServiceEnricher
    DTOBusinessValidator getBusinessValidator(); //FIXME change name
    Class<? extends IEvent> getServiceEvent();
}
