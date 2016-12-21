package com.springrollexample.router;

import com.springroll.core.ServiceDefinition;
import com.springroll.core.ServiceFactory;

/**
 * Created by anishjoseph on 07/10/16.
 */
public enum ApplicationServiceDefinitions implements ServiceDefinition {

    TEST_ROOT(TestRootServiceFactory.class),
    SYNCH_TO_ASYNCH(TestSynchFromAsynchServiceFactory.class);

    private Class<? extends ServiceFactory> serviceFactoryClass;
    private ServiceFactory serviceFactory;

    ApplicationServiceDefinitions(Class<? extends ServiceFactory> serviceFactoryClass){
        this.serviceFactoryClass = serviceFactoryClass;
    }

    public Class<? extends ServiceFactory> getServiceFactoryClass() {
        return serviceFactoryClass;
    }

    public void setServiceFactoryClass(Class<? extends ServiceFactory> serviceFactoryClass) {
        this.serviceFactoryClass = serviceFactoryClass;
    }

    public ServiceFactory getServiceFactory() {
        return serviceFactory;
    }

    public void setServiceFactory(ServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

}
