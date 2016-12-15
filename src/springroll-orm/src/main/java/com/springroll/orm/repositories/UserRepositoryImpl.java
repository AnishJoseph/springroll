package com.springroll.orm.repositories;

import com.springroll.orm.entities.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by anishjoseph on 26/09/16.
 */
public class UserRepositoryImpl implements UserRepositoryCustom {
    @Autowired
    UserRepository usersRepository;

    @Override
    public Collection<String> getRolesForUserId(String userId){
        User user = usersRepository.findByUserIdIgnoreCase(userId);
        if(user == null) return new ArrayList<>();
        return user.getRoles();
    }

    @Override
    public Set<String> findUsersThatBelongToRole(String role) {
        Set<String> users = usersRepository.findAll().stream()
                .filter(user -> user.getRoles().contains(role))
                .map(user -> user.getUserId())
                .collect(Collectors.toSet());
        return users;
    }

}
