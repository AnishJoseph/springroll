package com.springrollexample.core;

import com.springroll.core.Principal;

/**
 * Created by anishjoseph on 14/09/16.
 */
public interface IPrincipal extends Principal {
    String getUserId();
    String getRole();

}
