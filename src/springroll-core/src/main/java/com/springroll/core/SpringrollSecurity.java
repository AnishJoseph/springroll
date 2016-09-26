package com.springroll.core;

import org.springframework.security.core.userdetails.User;

/**
 * Created by anishjoseph on 14/09/16.
 */
public class SpringrollSecurity {
    public static User getUser(){
        return ContextStore.getUser();
    }
}
