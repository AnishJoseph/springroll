package com.springrollexample.core.security;

import com.springroll.core.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.List;

/**
 * Created by anishjoseph on 17/09/16.
 */
public class SpringrollExampleUser extends User implements Principal{
    private String name;
    private Collection<String> groups;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SpringrollExampleUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {

        super(username, password, authorities);
    }
    public SpringrollExampleUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public Collection<String> getGroups() {
        return groups;
    }

    public void setGroups(Collection<String> groups) {
        this.groups = groups;
    }
}
