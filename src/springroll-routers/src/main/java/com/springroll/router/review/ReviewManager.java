package com.springroll.router.review;

import com.springroll.core.BusinessValidationResult;
import com.springroll.orm.entities.ReviewStep;
import com.springroll.orm.entities.ReviewRules;
import com.springroll.orm.repositories.ReviewStepRepository;
import com.springroll.orm.repositories.ReviewRulesRepository;
import com.springroll.router.SpringrollEndPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anishjoseph on 27/09/16.
 */
@Service
public class ReviewManager extends SpringrollEndPoint {

    @Autowired
    ReviewRulesRepository reviewRulesRepository;

    @Autowired
    ReviewStepRepository reviewStepRepository;

    public void on(ReviewNeededEvent reviewNeededEvent){
        createReviewSteps(reviewNeededEvent.getPayload().getReviewNeededViolations(), reviewNeededEvent.getPayload().getEventForReview().getJobId());
        List<ReviewStep> nextReviewSteps = findNextReviewStep(reviewNeededEvent.getPayload().getEventForReview().getJobId(), 0);
        System.out.println(nextReviewSteps);
    }

    private void createReviewSteps(List<BusinessValidationResult> reviewNeededViolations, Long jobId){
        List<ReviewRules> reviewRules = new ArrayList<>();
        for (BusinessValidationResult businessValidationResult : reviewNeededViolations) {
            reviewRules.addAll(reviewRulesRepository.findByRuleName(businessValidationResult.getViolatedRule()));
        }
        List<ReviewStep> reviewSteps = new ArrayList<>();
        for (ReviewRules reviewRule : reviewRules) {
            ReviewStep reviewStep = new ReviewStep(reviewRule.getID(), reviewRule.getReviewStage(), jobId);
            reviewSteps.add(reviewStep);
        }
        reviewStepRepository.save(reviewSteps);
    }
    private List<ReviewStep> findNextReviewStep(Long jobId, int completedStepId){
        List<ReviewStep> allFutureSteps = reviewStepRepository.findByParentIdAndReviewStageIsGreaterThan(jobId, completedStepId);
        int minReviewStage = 10000;
        for (ReviewStep futureStep : allFutureSteps) {
            if(futureStep.getReviewStage() < minReviewStage)minReviewStage = futureStep.getReviewStage();
        }
        List<ReviewStep> nextSteps = reviewStepRepository.findByParentIdAndReviewStage(jobId, minReviewStage);
        return nextSteps;
    }
}
