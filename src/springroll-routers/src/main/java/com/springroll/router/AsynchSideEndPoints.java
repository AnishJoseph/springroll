package com.springroll.router;

import com.springroll.core.IEvent;
import org.apache.camel.EndpointInject;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by anishjoseph on 09/09/16.
 */
@Component
public class AsynchSideEndPoints {

    @EndpointInject(ref = "jmsEndPoint")
    private ProducerTemplate jmsEndPoint;

    @EndpointInject(ref = "dynamicRouterEndPoint")
    private ProducerTemplate dynamicRouterEndPoint;

    public void routeToJms(IEvent payload){
        jmsEndPoint.sendBody(jmsEndPoint.getDefaultEndpoint(), ExchangePattern.InOnly, payload);
    }
    public void routeToDynamicRouter(IEvent payload){
        dynamicRouterEndPoint.sendBody(dynamicRouterEndPoint.getDefaultEndpoint(), ExchangePattern.InOnly, payload);
    }
}