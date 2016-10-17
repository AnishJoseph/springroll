package com.springroll.api.facade.security;

import com.springroll.core.SpringrollUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

/**
 * Created by anishjoseph on 17/10/16.
 */
public class SwitchUserFilter extends org.springframework.security.web.authentication.switchuser.SwitchUserFilter{
    private static final Logger logger = LoggerFactory.getLogger(SwitchUserFilter.class);

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        /*if(((HttpServletRequest) req).getRequestURI().contains("login/impersonate")){
            SpringrollUser user = (SpringrollUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String realUser = user.getIsOfficiator()?user.getOfficiator():user.getUsername();
            String userId = req.getParameter(org.springframework.security.web.authentication.switchuser.SwitchUserFilter.SPRING_SECURITY_SWITCH_USERNAME_KEY);
            LocalDate currDate = new LocalDate().now();
            if(userId != null){
                String officiatedByUser = references.userDelegation.findIfUserCanbeOfficiatedByUser(userId, realUser,currDate);
                if(officiatedByUser == null){
                    logger.error("{} is attempting to become a delegate for {} - but is not authorized to do so", realUser, officiatedByUser);
                    ((HttpServletResponse) res).setStatus(HttpStatus.EXPECTATION_FAILED.value());
                    ((HttpServletRequest) req).getSession().invalidate();
                    return;
                }
            }
        }*/

        super.doFilter(req, res, chain);
    }
}
