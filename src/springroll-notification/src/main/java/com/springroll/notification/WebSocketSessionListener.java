package com.springroll.notification;

import com.springroll.core.SpringrollUser;
import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.ServerMessage;
import org.cometd.bayeux.server.ServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;

/**
 * Created by anishjoseph on 02/02/17.
 */
public class WebSocketSessionListener implements BayeuxServer.SessionListener {
    private SessionRegistry sessionRegistry;
    private static final Logger logger = LoggerFactory.getLogger(WebSocketSessionListener.class);
    public static final String SPRINGROLL_USER = "SpringrollUser";

    public void setSessionRegistry(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    public void sessionAdded(ServerSession session, ServerMessage message) {
        String jsessionId = (String)message.get("jsessionId");
        if(jsessionId != null){
            SessionInformation sessionInformation = sessionRegistry.getSessionInformation(jsessionId);
            if(sessionInformation != null){
                logger.trace("Web Socket Session : Added {} for user {}", session.getId(), ((SpringrollUser) sessionInformation.getPrincipal()).getUsername());
                session.setAttribute(SPRINGROLL_USER, (SpringrollUser)sessionInformation.getPrincipal());
            } else {
                logger.warn("jsessionId present in the message but unable to find a authenticated and registered Spring Security Session");
            }
        } else {
            logger.warn("Web Socket Session added but no jsessionId in the message");
        }
    }

    @Override
    public void sessionRemoved(ServerSession session, boolean timedout) {
        session.removeAttribute(SPRINGROLL_USER);
    }

}
