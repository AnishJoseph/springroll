package com.springroll.api.facade.security;

import com.springroll.core.ContextStore;
import com.springroll.core.SpringrollUser;
import com.springroll.orm.repositories.Repositories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by anishjoseph on 17/10/16.
 */
public class SwitchUserFilter extends org.springframework.security.web.authentication.switchuser.SwitchUserFilter{
    private static final Logger logger = LoggerFactory.getLogger(SwitchUserFilter.class);
    @Autowired private Repositories repositories;

    private SessionRegistry sessionRegistry;

    public void setSessionRegistry(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        boolean isImpersonateRequest = ((HttpServletRequest) req).getRequestURI().contains("login/impersonate");
        boolean isImpersonateLogout = ((HttpServletRequest) req).getRequestURI().contains("logout/impersonate");
        if( isImpersonateRequest) {
            SpringrollUser user = (SpringrollUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String realUser = user.isRunningAsDelegate()?user.getDelegator():user.getUsername();
            String userId = req.getParameter(org.springframework.security.web.authentication.switchuser.SwitchUserFilter.SPRING_SECURITY_SWITCH_USERNAME_KEY);
            LocalDate now = (LocalDate.now()).minusDays(1);
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
        /* For every request that comes in set the Context to that of the user that has logged in */
        ContextStore.put((SpringrollUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal(), null, null);

        super.doFilter(req, res, chain);
        if( isImpersonateRequest || isImpersonateLogout) {
            /* After the impersonation or after the impersonator logs out register the session id */
            sessionRegistry.registerNewSession(((HttpServletRequest) req).getSession().getId(), SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        }
    }
}
