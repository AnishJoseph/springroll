package com.springroll.orm.repositories;

import java.util.Collection;

/**
 * Created by anishjoseph on 26/09/16.
 */
public interface UsersRepositoryCustom {
    Collection<String> getRolesForUserId(String userId);
}
