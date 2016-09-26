package com.springrollexample.orm.helpers;

/**
 * Created by anishjoseph on 05/09/16.
 */

import com.springroll.orm.helpers.AbstractEntityRepository;
import com.springrollexample.orm.entities.Users;


public interface UsersRepository extends AbstractEntityRepository<Users>, UsersRepositoryCustom {
    Users findByUserIdIgnoreCase(String userId);

}
