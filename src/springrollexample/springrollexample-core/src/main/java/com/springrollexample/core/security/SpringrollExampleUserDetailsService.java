package com.springrollexample.core.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by anishjoseph on 17/09/16.
 */
public class SpringrollExampleUserDetailsService implements UserDetailsService, UserDetailsContextMapper, LdapAuthoritiesPopulator {
    private static final Logger logger = LoggerFactory.getLogger(SpringrollExampleUserDetailsService.class);

    private String mappedUserName = null;


    public void setMappedUserName(String mappedUserName) {
        this.mappedUserName = mappedUserName;
    }

    //Expects username in CAPS
    private SpringrollExampleUser loadUser(String username, Collection<? extends GrantedAuthority> authorities) throws UsernameNotFoundException {
        SpringrollExampleUser user = new SpringrollExampleUser(username, "dummyPassword", authorities);
        user.setName(username);
        user.setRole("ADMIN");
        return user;

    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loadUser(username.toUpperCase(), getGrantedAuthorities(null, username.toUpperCase()));
    }

    @Override   //From interface UserDetailsContextMapper
    public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection<? extends GrantedAuthority> authorities) {
        username = getUsername(ctx, username);
        return loadUser(username.toUpperCase(), authorities);
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
        username = getUsername(ctx, username);
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ADMIN"));

        return authorities;
    }
}