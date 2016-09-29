package com.springroll.orm.repositories;

/**
 * Created by anishjoseph on 05/09/16.
 */

import com.springroll.orm.repositories.AbstractEntityRepository;
import com.springroll.orm.entities.Groups;

import java.util.Collection;
import java.util.List;


public interface GroupsRepository extends AbstractEntityRepository<Groups> {

    List<Groups> findByGroupIn(Collection<String> groups);
}
