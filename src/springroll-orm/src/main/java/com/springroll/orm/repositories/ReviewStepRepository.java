package com.springroll.orm.repositories;

import com.springroll.orm.entities.ReviewStep;

import java.util.List;

/**
 * Created by anishjoseph on 12/09/16.
 */
public interface ReviewStepRepository extends AbstractEntityRepository<ReviewStep>{
    List<ReviewStep> findByParentIdAndReviewStageIsGreaterThan(Long parentId, int reviewStage);
    List<ReviewStep> findByCompletedIsFalseAndParentIdAndReviewStageIsLessThan(Long parentId, int reviewStage);
    List<ReviewStep> findByParentIdAndReviewStage(Long parentId, int reviewStage);
    ReviewStep findByParentIdAndSerializedEventIsNotNull(Long parentId);

}
