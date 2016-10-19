package com.springroll.orm.repositories;

import com.springroll.orm.entities.UserDelegation;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by anishjoseph on 12/09/16.
 */
public interface UserDelegationRepository extends AbstractEntityRepository<UserDelegation>{
   List<String> findDelegators(String user, LocalDate currDate);
   List<String> isValidDelegate(String delegateuserId, String delegatorUserId, LocalDate currDate);

}
