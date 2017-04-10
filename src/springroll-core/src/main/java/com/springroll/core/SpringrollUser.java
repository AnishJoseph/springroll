package com.springroll.core;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.*;

/**
 * Created by anishjoseph on 17/09/16.
 */
public class SpringrollUser extends User{
    private String displayName;
    private List<String> delegators = new ArrayList<>();    //User for whom i can be a delegate
    private String realLoggedInUser = null;
    private boolean runningAsDelegate = false;
    private Locale locale;
    private Set<String>  authorizations;

    public SpringrollUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {

        super(username, password, authorities);
    }
    public SpringrollUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<String> getDelegators() {
        return delegators;
    }

    public void setDelegators(List<String> delegators) {
        this.delegators = delegators;
    }

    public String getRealLoggedInUser() {
        return realLoggedInUser;
    }

    public void setRealLoggedInUser(String realLoggedInUser) {
        this.realLoggedInUser = realLoggedInUser;
    }

    public boolean isRunningAsDelegate() {
        return runningAsDelegate;
    }

    public void setRunningAsDelegate(boolean runningAsDelegate) {
        this.runningAsDelegate = runningAsDelegate;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Set<String> getAuthorizations() {
        return authorizations;
    }

    public void setAuthorizations(Set<String> authorizations) {
        this.authorizations = authorizations;
    }
}
