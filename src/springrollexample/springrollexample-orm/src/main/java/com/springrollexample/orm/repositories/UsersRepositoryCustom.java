package com.springrollexample.orm.repositories;

import java.util.Collection;
import java.util.List;

/**
 * Created by anishjoseph on 26/09/16.
 */
public interface UsersRepositoryCustom {
    Collection<String> getGroupsForUserId(String userId);
    List<String> getAuthoritiesForUserId(String userId);
}
