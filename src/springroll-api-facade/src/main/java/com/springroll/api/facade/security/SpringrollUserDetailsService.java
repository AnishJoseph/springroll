package com.springroll.api.facade.security;

import com.springroll.core.SpringrollUser;
import com.springroll.orm.entities.User;
import com.springroll.orm.repositories.Repositories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.switchuser.SwitchUserGrantedAuthority;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Created by anishjoseph on 17/09/16.
 */
public class SpringrollUserDetailsService implements UserDetailsService, UserDetailsContextMapper, LdapAuthoritiesPopulator, AuthenticationSuccessHandler {
    private static final Logger logger = LoggerFactory.getLogger(SpringrollUserDetailsService.class);
    @Autowired Repositories repo;

    private String mappedUserName = null;
    private String mappedDisplayName = null;


    public void setMappedUserName(String mappedUserName) {
        this.mappedUserName = mappedUserName;
    }

    public void setMappedDisplayName(String mappedDisplayName) {
        this.mappedDisplayName = mappedDisplayName;
    }

    //Expects username in CAPS
    private SpringrollUser loadUser(String username, Collection<? extends GrantedAuthority> authorities) throws UsernameNotFoundException {
        /* Comes here on both login and on switch user */
        SpringrollUser user = new SpringrollUser(username, "dummyPassword", authorities);
        User userInDb = repo.users.findByUserIdIgnoreCase(username);
        Locale locale;
        String country = userInDb.getCountry() == null ? "": userInDb.getCountry();
        String variant = userInDb.getVariant() == null ? "": userInDb.getVariant();
        if(userInDb.getLanguage() == null){
            locale = Locale.getDefault();
        } else {
            locale = new Locale(userInDb.getLanguage(), country, variant);
        }
        user.setLocale(locale);
        return user;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Comes here when a user is switched
        SpringrollUser springrollUser = loadUser(username.toUpperCase(), getGrantedAuthorities(null, username.toUpperCase()));
        springrollUser.setDisplayName(username);
        return springrollUser;
    }

    @Override   //From interface UserDetailsContextMapper
    public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
        //Comes here only on login NOT when a user is switched
        username = getUsername(ctx, username);
        SpringrollUser user = loadUser(username.toUpperCase(), authorities);
        LocalDate now = (LocalDate.now()).minusDays(1);
        List<String> delegators = repo.delegation.findDelegators(user.getUsername(), now);
        user.setDelegators(delegators);
        if(mappedDisplayName == null){
            user.setDisplayName(username);
            return user;
        }
        try {
            String displayName = (String) ctx.getAttributes().get(mappedDisplayName).get(0);
            user.setDisplayName(displayName);
        }catch (Exception e){
            user.setDisplayName(username);
        }
        return user;
    }

    @Override  //From interface UserDetailsContextMapper
    public void mapUserToContext(UserDetails user, DirContextAdapter ctx) {
    }

    private String getUsername(DirContextOperations ctx, String username){
        if(mappedUserName != null && !mappedUserName.isEmpty()){
            try {
                username = (String) (ctx.getAttributes().get(mappedUserName).get(0));
            }catch (NamingException e){
                logger.error("Unable to map attribute {} to received ldap attributes ", mappedUserName);
                throw new UsernameNotFoundException(String.format("Unable to map attribute {} to received ldap attributes", mappedUserName));
            }
        }
        return username;
    }
    @Override   //from interface LdapAuthoritiesPopulator
    public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations ctx, String username) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        username = getUsername(ctx, username);
        Collection<String> authoritiesForUserId = repo.users.getRolesForUserId(username);
        authorities.addAll(authoritiesForUserId.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        return authorities;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        /* Only comes here on switching a user - not when a user just logs in */
        response.sendRedirect("/");
        SpringrollUser user = (SpringrollUser)authentication.getPrincipal();
        for (GrantedAuthority authority : authentication.getAuthorities()) {

            String authorityAsString = authority.getAuthority();

            if ("ROLE_PREVIOUS_ADMINISTRATOR".equals(authorityAsString)) {
                user.setRunningAsDelegate(true);
                SpringrollUser oldUser = (SpringrollUser) ((SwitchUserGrantedAuthority) authority).getSource().getPrincipal();
                user.setDelegator(oldUser.getUsername());
                List<String> switchToPossibilities = new ArrayList<>();
                switchToPossibilities.addAll(oldUser.getDelegators());
                switchToPossibilities.remove(user.getUsername());
                switchToPossibilities.add(oldUser.getUsername());
                user.setDelegators(switchToPossibilities);
            }
        }
    }
}