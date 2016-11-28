package com.springroll.orm.repositories;

import com.springroll.orm.entities.ReviewStepMeta;

import java.util.List;

/**
 * Created by anishjoseph on 12/09/16.
 */
public interface ReviewStepMetaRepository extends AbstractEntityRepository<ReviewStepMeta>{
        List<ReviewStepMeta> findBySearchId(String searchId);
        ReviewStepMeta findByParentId(Long parentId);


}
