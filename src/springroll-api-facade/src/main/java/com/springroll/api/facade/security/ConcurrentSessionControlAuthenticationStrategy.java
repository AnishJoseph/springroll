package com.springroll.api.facade.security;

import com.springroll.core.LocaleFactory;
import com.springroll.core.SpringrollUser;
import com.springroll.orm.repositories.Repositories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by anishjoseph on 03/02/17.
 */
public class ConcurrentSessionControlAuthenticationStrategy extends org.springframework.security.web.authentication.session.ConcurrentSessionControlAuthenticationStrategy {
    boolean blockDelegatedUsers = true;
    private static final Logger logger = LoggerFactory.getLogger(ConcurrentSessionControlAuthenticationStrategy.class);
    @Autowired Repositories repositories;

    public void setBlockDelegatedUsers(boolean blockDelegatedUsers) {
        this.blockDelegatedUsers = blockDelegatedUsers;
    }

    public ConcurrentSessionControlAuthenticationStrategy(SessionRegistry sessionRegistry) {
        super(sessionRegistry);
    }
    public void onAuthentication(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {

        if(blockDelegatedUsers) {
            List<String> myDelegates = repositories.delegation.getMyDelegates(((SpringrollUser) authentication.getPrincipal()).getUsername(), LocalDate.now());
            if(!myDelegates.isEmpty()) {
                logger.error("User {} was blocked from logging in as {} is officiating",((SpringrollUser) authentication.getPrincipal()).getUsername(), myDelegates.get(0) );
                String message = LocaleFactory.getLocalizedMessage(((SpringrollUser) authentication.getPrincipal()).getLocale(), "security.delegated", ((SpringrollUser) authentication.getPrincipal()).getUsername(), myDelegates.get(0));
                throw new SessionAuthenticationException(message);
            }
        }
        super.onAuthentication(authentication, request, response);
        return;
    }
}
