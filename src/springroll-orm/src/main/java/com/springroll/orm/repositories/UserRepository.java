package com.springroll.orm.repositories;

/**
 * Created by anishjoseph on 05/09/16.
 */

import com.springroll.orm.entities.User;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

/*
private static final String QUERY = "select b from Branch b"
       + " left join b.filial f"
       + " where f.id = ?1 and b.id like CONCAT(?2, '%')";
    @Query(QUERY)
    List<Branch> findByFilialAndBranchLike(String filialId, String branchCode);
 */

public interface UserRepository extends AbstractEntityRepository<User>, UserRepositoryCustom {
    User findByUserIdIgnoreCase(String userId);
}
