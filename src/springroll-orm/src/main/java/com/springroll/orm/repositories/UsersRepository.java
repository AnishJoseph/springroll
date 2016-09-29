package com.springroll.orm.repositories;

/**
 * Created by anishjoseph on 05/09/16.
 */

import com.springroll.orm.entities.Users;


public interface UsersRepository extends AbstractEntityRepository<Users>, UsersRepositoryCustom {
    Users findByUserIdIgnoreCase(String userId);

}
