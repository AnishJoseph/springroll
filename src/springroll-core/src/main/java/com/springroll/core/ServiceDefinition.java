package com.springroll.core;

/**
 * Created by anishjoseph on 07/10/16.
 */
public interface ServiceDefinition {
    Class<? extends ServiceFactory> getServiceFactoryClass();

    void setServiceFactoryClass(Class<? extends ServiceFactory> serviceFactoryClass);

    ServiceFactory getServiceFactory();

    void setServiceFactory(ServiceFactory serviceFactory);
    String name();
}
