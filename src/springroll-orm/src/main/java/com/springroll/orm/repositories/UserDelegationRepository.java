package com.springroll.orm.repositories;

import com.springroll.orm.entities.ReviewRule;
import com.springroll.orm.entities.UserDelegation;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by anishjoseph on 12/09/16.
 */
public interface UserDelegationRepository extends AbstractEntityRepository<UserDelegation>{
   List<String> findDelegators(String user, LocalDateTime currDate);
   List<String> isValidDelegate(String delegateuserId, String delegatorUserId, LocalDateTime currDate);

}
