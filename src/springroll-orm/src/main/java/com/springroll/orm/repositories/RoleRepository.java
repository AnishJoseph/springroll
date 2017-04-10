package com.springroll.orm.repositories;

/**
 * Created by anishjoseph on 05/09/16.
 */

import com.springroll.orm.entities.Role;

import java.util.List;


public interface RoleRepository extends AbstractEntityRepository<Role> {
    List<List<String>> getAuthorizationsForRoles(List<String> roles);

}
