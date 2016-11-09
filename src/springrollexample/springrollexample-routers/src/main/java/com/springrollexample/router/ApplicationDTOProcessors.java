package com.springrollexample.router;

import com.springroll.core.IDTOProcessors;
import com.springroll.core.IServiceFactory;

/**
 * Created by anishjoseph on 07/10/16.
 */
public enum ApplicationDTOProcessors implements IDTOProcessors {

    TEST_ROOT(ITestRootServiceFactory.class),
    SYNCH_TO_ASYNCH(ITestSynchFromAsynchServiceFactory.class);

    private Class<? extends IServiceFactory> serviceFactoryClass;
    private IServiceFactory serviceFactory;

    ApplicationDTOProcessors(Class<? extends IServiceFactory> serviceFactoryClass){
        this.serviceFactoryClass = serviceFactoryClass;
    }

    public Class<? extends IServiceFactory> getServiceFactoryClass() {
        return serviceFactoryClass;
    }

    public void setServiceFactoryClass(Class<? extends IServiceFactory> serviceFactoryClass) {
        this.serviceFactoryClass = serviceFactoryClass;
    }

    public IServiceFactory getServiceFactory() {
        return serviceFactory;
    }

    public void setServiceFactory(IServiceFactory serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

}
