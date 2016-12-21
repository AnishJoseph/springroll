package com.springroll.router;

import com.springroll.core.ServiceDefinition;
import com.springroll.core.ServiceFactory;
import com.springroll.core.services.mdm.MdmServiceFactory;
import com.springroll.core.services.notification.NotificationServiceFactory;
import com.springroll.core.services.review.ReviewServiceFactory;

/**
 * Created by anishjoseph on 07/10/16.
 */
public enum CoreServiceDefinitions implements ServiceDefinition {
    REVIEW(ReviewServiceFactory.class),
    NOTIFICATION_ACK(NotificationServiceFactory.class),
    MDM(MdmServiceFactory.class);

    private Class<? extends ServiceFactory> serviceFactoryClass;
    private ServiceFactory serviceFactory;

    CoreServiceDefinitions(Class<? extends ServiceFactory> serviceFactoryClass){
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
