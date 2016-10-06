package com.springroll.orm.repositories;

import com.springroll.orm.entities.Users;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by anishjoseph on 26/09/16.
 */
public class UsersRepositoryImpl implements UsersRepositoryCustom {
    @Autowired
    UsersRepository usersRepository;

    @Autowired
    RolesRepository rolesRepository;

    @Override
    public Collection<String> getRolesForUserId(String userId){
        Users user = usersRepository.findByUserIdIgnoreCase(userId);
        if(user == null) return new ArrayList<>();
        return user.getRoles();
    }
}
