package com.springroll.core;

/**
 * Created by anishjoseph on 14/09/16.
 */
public class SpringrollSecurity {
    public static Principal getPrincipal(){
        return ContextStore.getPrincipal();
    }
}
