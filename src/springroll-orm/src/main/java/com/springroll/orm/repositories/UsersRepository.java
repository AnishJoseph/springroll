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
    //FIXME - make this a named query
    static final String findUsersThatBelongToRole = "select user.userId from Users as user where user.roles like ?1";


    Users findByUserIdIgnoreCase(String userId);
    @Query(findUsersThatBelongToRole)
    Set<String> findUsersThatBelongToRole(String role);

}
