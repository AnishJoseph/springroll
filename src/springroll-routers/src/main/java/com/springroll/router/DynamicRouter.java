package com.springroll.router;

import org.apache.camel.Properties;
import com.springroll.core.DTO;
import com.springroll.core.IEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by anishjoseph on 10/09/16.
 */
@Service
public class DynamicRouter {
    private static final Logger logger = LoggerFactory.getLogger(DynamicRouter.class);

    @Autowired
    private ApplicationContext applicationContext;

    private Map<Class<? extends IEvent<DTO>>, List<Subscription>> routes = new HashMap<Class<? extends IEvent<DTO>>, List<Subscription>>();

    @Autowired
    AsynchSideEndPoints asynchEndPoint;

    @Autowired
    JobManager jobManager;

    @PersistenceContext
    EntityManager em;

    @PostConstruct
    public void init() {
        Map<String, SpringrollEndPoint> componentsMap = applicationContext.getBeansOfType(SpringrollEndPoint.class);

        if (componentsMap == null || componentsMap.isEmpty()) return;

        for (String componentBeanName : componentsMap.keySet()) {
            SpringrollEndPoint springrollEndPoint = componentsMap.get(componentBeanName);
            Class<? extends SpringrollEndPoint> componentClass = springrollEndPoint.getClass();

            List<Subscription> eventSubscriptions = findSubscriberMethods(componentClass);

            for (Subscription subscription : eventSubscriptions) {
                subscription.setComponentBeanName(componentBeanName);

                List<Subscription> subscriptions = routes.get(subscription.getEventClass());
                if (subscriptions == null) {
                    subscriptions = new ArrayList<Subscription>();
                    routes.put(subscription.getEventClass(), subscriptions);
                }

                logger.debug("Found subscription : {}", subscription);
                subscriptions.add(subscription);
            }
            logger.info("Added {} event subscriptions for {} component.", eventSubscriptions.size(), componentBeanName);
        }
    }

    public String route(IEvent<DTO> event, @Properties Map<String, Object> properties) {
        /*
            A dynamic router works on the principle that once an event is routed, the control
            will come back to the Router to find out if there are any more routes the event
            has to go to. So we make use of the properties hash map to notify that a particular
            event is already routed.
         */

        Object invoked = properties.get(this.getClass().getSimpleName());

        if (invoked != null) {
            em.flush();
            return null;
        }
        /* We set in the properties hashmap that the bean has undergone routing */

        properties.put(this.getClass().getSimpleName(), true);

        /* If the event is rerouted, then the event will already know the destination bean it has to go to. So we directly return the destination bean value */

        if (event.getDestinationUri() != null && event.isRouted() == true) {
            StringBuilder uriBuilder = new StringBuilder();
            uriBuilder.append(event.getDestinationUri());
            return uriBuilder.toString();
        }

        Collection<Subscription> subscriptions = findSubscriptions(event.getClass());

        if (subscriptions == null || subscriptions.isEmpty()) {
            return null;
        }

        StringBuilder uriBuilder = new StringBuilder();


        for (Subscription subscription : subscriptions) {
            /*
                Check if the event listener set in the subscriptions variable has been marked for a new transcation,
                if yes then set the destination bean and reroute it back through p1Router over the JMS endpoint
             */

            if (subscription.isNewTransaction()) {
                IEvent eventToSendToJMS;
                try {
                    eventToSendToJMS = event.getClass().newInstance();
                } catch (Exception e) {
                    if (e instanceof RuntimeException) throw (RuntimeException) e;
                    throw new RuntimeException(e);
                }
                eventToSendToJMS.setJobId(event.getJobId());
                eventToSendToJMS.setPayloads(event.getPayloads());
                eventToSendToJMS.setPrincipal(event.getPrincipal());

                eventToSendToJMS.setDestinationUri(subscription.getUri());
                eventToSendToJMS.setRouted(true);
                eventToSendToJMS.setLegId(jobManager.registerNewTransactionLeg(eventToSendToJMS.getJobId()));
                logger.debug("JobId {} - Will route {} to {} later as it was @NewTransaction", new Object[]{event.getJobId(), event.getClass().getSimpleName(), subscription});
                asynchEndPoint.routeToJms(eventToSendToJMS);
                continue;
            }

            logger.debug("JobId {}, LegId {}. Routing Event '{}' to {}", new Object[]{event.getJobId(), event.getLegId(), event.getClass().getSimpleName(), subscription});

            if (uriBuilder.length() != 0)
                uriBuilder.append(",");

            uriBuilder.append(subscription.getUri());

        }

        if (uriBuilder.toString().length() > 0) {
            return uriBuilder.toString();
        }
        return null;
    }

    protected Collection<Subscription> findSubscriptions(Class<?> eventClass) {

        Set<Subscription> subscriptions = new HashSet<Subscription>();

        List<Subscription> subs = routes.get(eventClass);

        if (subs != null) {
            for (Subscription sub : subs) {
                subscriptions.add(sub);
            }
        }

        for (Class<? extends IEvent<DTO>> clazz : routes.keySet()) {
            if (clazz.isInterface()) {
                if (clazz.isAssignableFrom(eventClass)) {
                    subs = routes.get(clazz);
                    if (subs != null) {
                        for (Subscription sub : subs) {
                            subscriptions.add(sub);
                        }
                    }
                }
            }
        }

        return subscriptions;
    }

    protected List<Subscription> findSubscriberMethods(Class<? extends SpringrollEndPoint> clazz) {

        List<Subscription> eventSubscriptions = new ArrayList<Subscription>();

        Class<?> searchType = clazz;

        while (searchType != null) {
            Method[] methods = searchType.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getParameterTypes().length == 1 && method.getName().equals("on")) {
                    Class<?> parameterType = method.getParameterTypes()[0];
                    if (IEvent.class.isAssignableFrom(parameterType)) {

                        @SuppressWarnings("unchecked")
                        Class<? extends IEvent<DTO>> eClass = (Class<? extends IEvent<DTO>>) parameterType;


                        Subscription subscription = new Subscription();
                        subscription.setComponentClass(clazz);
                        subscription.setEventClass(eClass);
                        subscription.setMethodName(method.getName());

                        /*
                        Check if the event listener has been annotated with NewTransaction.
                         */

                        if (method.isAnnotationPresent(NewTransaction.class)) {
                            NewTransaction annotation = method.getAnnotation(NewTransaction.class);
                            subscription.setNewTransaction(annotation.value());

                        }

                        eventSubscriptions.add(subscription);
                    }
                }
            }
            searchType = searchType.getSuperclass();
        }
        return eventSubscriptions;
    }

    public class Subscription {
        private Class<? extends SpringrollEndPoint> componentClass;
        private Class<? extends IEvent<DTO>> eventClass;
        private String methodName;
        private String componentBeanName = null;
        private String uri;
        private boolean isNewTransaction = false;

        public Class<? extends IEvent<DTO>> getEventClass() {
            return eventClass;
        }

        public String getUri() {
            if (this.uri == null)
                this.uri = "bean:" + componentBeanName;
            return this.uri;
        }

        @Override
        public String toString() {
            if (componentBeanName == null) return "Not Yet Inited";
            return componentClass.getSimpleName() + "." + methodName + "(" + eventClass.getSimpleName() + ") - URI is " + getUri();
        }

        public void setComponentBeanName(String componentBeanName) {
            this.componentBeanName = componentBeanName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public void setEventClass(Class<? extends IEvent<DTO>> eventClass) {
            this.eventClass = eventClass;
        }

        public void setComponentClass(Class<? extends SpringrollEndPoint> componentClass) {
            this.componentClass = componentClass;
        }

        public boolean isNewTransaction() {
            return isNewTransaction;
        }

        public void setNewTransaction(boolean newTransaction) {
            isNewTransaction = newTransaction;
        }
    }
}