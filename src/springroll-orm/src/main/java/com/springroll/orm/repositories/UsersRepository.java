package com.springroll.orm.repositories;

/**
 * Created by anishjoseph on 05/09/16.
 */

import com.springroll.orm.entities.Users;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

/*
private static final String QUERY = "select b from Branch b"
       + " left join b.filial f"
       + " where f.id = ?1 and b.id like CONCAT(?2, '%')";
    @Query(QUERY)
    List<Branch> findByFilialAndBranchLike(String filialId, String branchCode);
 */

public interface UsersRepository extends AbstractEntityRepository<Users>, UsersRepositoryCustom {
    static final String findUsersThatBelongToGroupQuery = "select user.userId from Users as user where user.grps like ?1";


    Users findByUserIdIgnoreCase(String userId);
    @Query(findUsersThatBelongToGroupQuery)
    Set<String> findUsersThatBelongToGroup(String group);

}
