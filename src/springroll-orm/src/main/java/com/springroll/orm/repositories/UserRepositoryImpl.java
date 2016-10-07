package com.springroll.orm.repositories;

import com.springroll.orm.entities.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by anishjoseph on 26/09/16.
 */
public class UserRepositoryImpl implements UserRepositoryCustom {
    @Autowired
    UserRepository usersRepository;

    @Autowired
    RoleRepository rolesRepository;

    @Override
    public Collection<String> getRolesForUserId(String userId){
        User user = usersRepository.findByUserIdIgnoreCase(userId);
        if(user == null) return new ArrayList<>();
        return user.getRoles();
    }
}
