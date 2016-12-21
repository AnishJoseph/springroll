package com.springroll.router;

import com.springroll.core.IServiceDefinition;
import com.springroll.core.IServiceFactory;
import com.springroll.core.services.IMdmServiceFactory;
import com.springroll.core.services.notification.INotificationServiceFactory;
import com.springroll.core.services.IReviewServiceFactory;

/**
 * Created by anishjoseph on 07/10/16.
 */
public enum CoreServiceDefinitions implements IServiceDefinition {
    REVIEW(IReviewServiceFactory.class),
    NOTIFICATION_ACK(INotificationServiceFactory.class),
    MDM(IMdmServiceFactory.class);

    private Class<? extends IServiceFactory> serviceFactoryClass;
    private IServiceFactory serviceFactory;

    CoreServiceDefinitions(Class<? extends IServiceFactory> serviceFactoryClass){
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
