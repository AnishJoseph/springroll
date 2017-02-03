package com.springroll.api.facade.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by anishjoseph on 03/02/17.
 */
public class RegisterSessionAuthenticationStrategy implements SessionAuthenticationStrategy {
    private final SessionRegistry sessionRegistry;
    private final SessionRegistry sessionRegistryForDelegation;

    private static final Logger logger = LoggerFactory.getLogger(RegisterSessionAuthenticationStrategy.class);

    /**
     * @param sessionRegistry the session registry which should be updated when the authenticated session is changed.
     */
    public RegisterSessionAuthenticationStrategy(SessionRegistry sessionRegistry, SessionRegistry sessionRegistryForDelegation) {
        Assert.notNull(sessionRegistry, "The sessionRegistry cannot be null");
        this.sessionRegistry = sessionRegistry;
        this.sessionRegistryForDelegation = sessionRegistryForDelegation;
    }

    /**
     * In addition to the steps from the superclass, the sessionRegistry will be updated with the new session information.
     */
    public void onAuthentication(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        sessionRegistry.registerNewSession(request.getSession().getId(), authentication.getPrincipal());
        sessionRegistryForDelegation.registerNewSession(request.getSession().getId(), authentication.getPrincipal());
        return;
    }
}
