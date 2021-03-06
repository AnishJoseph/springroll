package com.springroll.router;

import com.springroll.core.IEvent;
import org.apache.camel.EndpointInject;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * This bean holds the endpoints for the asynchronous side of Springroll.
 * Use this bean to send IEvents either<p>
 * a) directly to DynamicRouter - in which case it will be synchronous and the event will be handled in the same thread<p>
 * b) to JMS (to a queue called asynchronousEndPoint) - in this case the event will be handled in a separate thread
 *
 * @author Anish Joseph
 * @since 1.0
 */
@Component
public class AsynchSideEndPoints {

    @EndpointInject(ref = "jmsEndPoint")
    private ProducerTemplate jmsEndPoint;

    @EndpointInject(ref = "dynamicRouterEndPoint")
    private ProducerTemplate dynamicRouterEndPoint;

    @PersistenceContext
    EntityManager entityManager;

    /**
     * Route an event (IEvent) to JMS. This endpoint is InOnly - i.e there is no return value
     * Since the event is delivered via JMS, it will be processed in another thread (and another transaction)<p></p>
     * <b>Note: </b> Delivery of the message ONLY happens IF the current transaction commits
     * @param event - Object of type IEvent
     */
    public void routeToJms(IEvent event){
        entityManager.flush();
        jmsEndPoint.sendBody(jmsEndPoint.getDefaultEndpoint(), ExchangePattern.InOnly, event);
    }

    /**
     * Route an event directly to 'DynamicRouter' - this happens synchronously and will be processed in
     * the same transaction as the caller.
     * @param event - Object of type IEvent
     */
    public void routeToDynamicRouter(IEvent event){
        entityManager.flush();
        dynamicRouterEndPoint.sendBody(dynamicRouterEndPoint.getDefaultEndpoint(), ExchangePattern.InOnly, event);
    }
}