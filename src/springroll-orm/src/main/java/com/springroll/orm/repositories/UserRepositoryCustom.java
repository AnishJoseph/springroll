package com.springroll.orm.repositories;

import java.util.Collection;
import java.util.Set;

/**
 * Created by anishjoseph on 26/09/16.
 */
public interface UserRepositoryCustom {
    Collection<String> getRolesForUserId(String userId);
    Set<String> findUsersThatBelongToRole(String role);
}
