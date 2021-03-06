package com.springroll.notification;

import com.springroll.core.CustomSpringrollJSONSerializers;
import org.cometd.annotation.ServerAnnotationProcessor;
import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.common.JSONContext;
import org.cometd.server.AbstractServerTransport;
import org.cometd.server.BayeuxServerImpl;
import org.cometd.server.Jackson2JSONContextServer;
import org.cometd.server.transport.JSONPTransport;
import org.cometd.server.transport.JSONTransport;
import org.cometd.websocket.server.WebSocketTransport;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.ServletContext;

/**
 * Created by anishjoseph on 01/10/16.
 */
@Component
public class CometDInitializer implements ServletContextAware
{
    @Autowired SessionRegistry sessionRegistry;
    @Autowired WebSocketSessionListener webSocketSessionListener;

    private ServletContext servletContext;

    @Bean(initMethod = "start", destroyMethod = "stop")
    public BayeuxServer bayeuxServer()
    {
        BayeuxServerImpl bean = new BayeuxServerImpl();
        bean.setTransports(new WebSocketTransport(bean), new JSONTransport(bean), new JSONPTransport(bean));
        servletContext.setAttribute(BayeuxServer.ATTRIBUTE, bean);
        JSONContext.Server jsonContext = new Jackson2JSONContextServer();
        ((Jackson2JSONContextServer)jsonContext).getObjectMapper().registerModule(new CustomSpringrollJSONSerializers());
        bean.setOption(AbstractServerTransport.JSON_CONTEXT_OPTION, jsonContext);

        bean.setOption(ServletContext.class.getName(), servletContext);
        bean.setOption("ws.cometdURLMapping", "/cometd/*");
        bean.addListener(webSocketSessionListener);
        return bean;
    }

    public void setServletContext(ServletContext servletContext)
    {
        this.servletContext = servletContext;
    }

    @Component
    public static class Processor implements DestructionAwareBeanPostProcessor
    {
        @Autowired
        private BayeuxServer bayeuxServer;
        private ServerAnnotationProcessor processor;

        @PostConstruct
        private void init()
        {
            this.processor = new ServerAnnotationProcessor(bayeuxServer);
        }

        @PreDestroy
        private void destroy()
        {
        }

        public Object postProcessBeforeInitialization(Object bean, String name) throws BeansException
        {
            processor.processDependencies(bean);
            processor.processConfigurations(bean);
            processor.processCallbacks(bean);
            return bean;
        }

        public Object postProcessAfterInitialization(Object bean, String name) throws BeansException
        {
            return bean;
        }

        public void postProcessBeforeDestruction(Object bean, String name) throws BeansException
        {
            processor.deprocessCallbacks(bean);
        }
        public boolean requiresDestruction(Object bean){
            return true;

        }
    }
}