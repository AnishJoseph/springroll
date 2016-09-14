package com.springrollexample.core;

import com.springroll.core.Principal;

/**
 * Created by anishjoseph on 14/09/16.
 */
public class ExamplePrincipal implements IPrincipal {
    private static final long serialVersionUID = 1L;
    private String userId;
    private String role;

    public ExamplePrincipal(String userId, String role) {
        this.userId = userId;
        this.role = role;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
