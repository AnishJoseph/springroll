package com.springroll.orm.repositories;

import com.springroll.orm.entities.Groups;
import com.springroll.orm.entities.Users;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by anishjoseph on 26/09/16.
 */
public class UsersRepositoryImpl implements UsersRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    UsersRepository usersRepository;

    @Autowired
    GroupsRepository groupsRepository;

    @Override
    public Collection<String> getGroupsForUserId(String userId){
        Users user = usersRepository.findByUserIdIgnoreCase(userId);
        if(user == null) return new ArrayList<>();
        return user.getGroups();
    }
    @Override
    public List<String> getAuthoritiesForUserId(String userId){
        Collection<String> groupsForUserId = getGroupsForUserId(userId);
        List<Groups> groups = groupsRepository.findByGroupIn(groupsForUserId);
        List<String> authorities = new ArrayList<>();
        for (Groups group : groups) {
            authorities.addAll(group.getAuths());
        }

        return authorities;
    }
}
