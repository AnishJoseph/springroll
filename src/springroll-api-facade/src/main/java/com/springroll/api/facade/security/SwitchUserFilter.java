package com.springroll.api.facade.security;

import com.springroll.core.SpringrollUser;
import com.springroll.orm.repositories.Repositories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by anishjoseph on 17/10/16.
 */
public class SwitchUserFilter extends org.springframework.security.web.authentication.switchuser.SwitchUserFilter{
    private static final Logger logger = LoggerFactory.getLogger(SwitchUserFilter.class);
    @Autowired Repositories repositories;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        if(((HttpServletRequest) req).getRequestURI().contains("login/impersonate")){
            SpringrollUser user = (SpringrollUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String realUser = user.isRunningAsDelegate()?user.getDelegator():user.getUsername();
            String userId = req.getParameter(org.springframework.security.web.authentication.switchuser.SwitchUserFilter.SPRING_SECURITY_SWITCH_USERNAME_KEY);
            LocalDateTime now = (LocalDateTime.now()).minusDays(1);
            if(userId != null){
                List<String> officiatedByUser = repositories.delegation.isValidDelegate(realUser, userId, now);
                if(officiatedByUser.isEmpty()){
                    logger.error("{} is attempting to become a delegate for {} - but is not authorized to do so", realUser, userId);
                    ((HttpServletResponse) res).setStatus(HttpStatus.EXPECTATION_FAILED.value());
                    ((HttpServletRequest) req).getSession().invalidate();
                    return;
                }
            }
        }

        super.doFilter(req, res, chain);
    }
}
