package com.springroll.orm.repositories;

import com.springroll.orm.entities.Reviews;

import java.util.List;

/**
 * Created by anishjoseph on 12/09/16.
 */
public interface ReviewsRepository extends AbstractEntityRepository<Reviews>{
   List<Reviews> findByParentIdAndMaster(Long parentId, String master);
}
