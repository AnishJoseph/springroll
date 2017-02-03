package com.springroll.api.facade.security;

import com.springroll.core.SpringrollUser;
import com.springroll.core.services.push.WebSocketSessionRegistry;
import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.ServerMessage;
import org.cometd.bayeux.server.ServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by anishjoseph on 02/02/17.
 */
public class WebSocketSessionListener implements BayeuxServer.SessionListener, WebSocketSessionRegistry {
    private SessionRegistry sessionRegistry;
    private Map<String, SpringrollUser> websocketSessionToUserMap = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(WebSocketSessionListener.class);

    public void setSessionRegistry(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    public void sessionAdded(ServerSession session, ServerMessage message) {
        String jsessionId = (String)message.get("jsessionId");
        if(jsessionId != null){
            SessionInformation sessionInformation = sessionRegistry.getSessionInformation(jsessionId);
            if(sessionInformation != null){
                websocketSessionToUserMap.put(session.getId(), (SpringrollUser)sessionInformation.getPrincipal());
            } else {
                logger.warn("jsessionId present in the message but unable to find a authenticated and registered Spring Security Session");
            }
        } else {
            logger.warn("Web Socket Session added but no jsessionId in the message");
        }
    }

    @Override
    public void sessionRemoved(ServerSession session, boolean timedout) {
        SpringrollUser user = websocketSessionToUserMap.get(session.getId());
        logger.debug("Web Socket Session removed {}", session.getId(), user == null ? "Unknown Session" : user.getUsername() );
        websocketSessionToUserMap.remove(session.getId());
    }

    @Override
    public SpringrollUser getUserForSessionId(String sessionId){
        return websocketSessionToUserMap.get(sessionId);
    }

}
