package com.springroll.core;

/**
 * Created by anishjoseph on 07/10/16.
 */
public interface ServiceDefinition {
    Class<? extends IServiceFactory> getServiceFactoryClass();

    void setServiceFactoryClass(Class<? extends IServiceFactory> serviceFactoryClass);

    IServiceFactory getServiceFactory();

    void setServiceFactory(IServiceFactory serviceFactory);
    String name();
}
