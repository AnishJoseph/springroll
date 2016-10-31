package com.springroll.orm.repositories;

import com.springroll.orm.entities.ReviewStep;

import java.util.List;

/**
 * Created by anishjoseph on 12/09/16.
 */
public interface ReviewStepRepository extends AbstractEntityRepository<ReviewStep>{
    List<ReviewStep> findByParentIdAndReviewStageIsGreaterThan(Long parentId, int reviewStage);
    //FIXME - when col is 't' or 'f this does not work findByCompletedIsFalseAndParentIdAndReviewStageIsLessThan
    List<ReviewStep> findByCompletedAndParentIdAndReviewStageIsLessThan(boolean completed, Long parentId, int reviewStage);
    List<ReviewStep> findByParentIdAndReviewStage(Long parentId, int reviewStage);
    List<ReviewStep> findByParentId(Long parentId);
    ReviewStep findByParentIdAndSerializedEventIsNotNull(Long parentId);

}
