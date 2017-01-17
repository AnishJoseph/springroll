package com.springroll.orm.repositories;

import com.springroll.orm.entities.ReviewStep;

import java.util.List;
import java.util.Set;

/**
 * Created by anishjoseph on 12/09/16.
 */
public interface ReviewStepRepository extends AbstractEntityRepository<ReviewStep>{
    List<ReviewStep> findByParentIdAndReviewStageIsGreaterThan(Long parentId, int reviewStage);
    Set<String> getReviewers(Long parentId);
    //FIXME - when col is 't' or 'f this does not work findByCompletedIsFalseAndParentIdAndReviewStageIsLessThan
    List<ReviewStep> findByCompletedIsFalseAndParentIdAndReviewStageIsLessThan(Long parentId, int reviewStage);
    List<ReviewStep> findByParentIdAndReviewStage(Long parentId, int reviewStage);
    List<ReviewStep> findByParentId(Long parentId);

}
