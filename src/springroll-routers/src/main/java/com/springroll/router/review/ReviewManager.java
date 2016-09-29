package com.springroll.router.review;

import com.springroll.core.BusinessValidationResult;
import com.springroll.core.ContextStore;
import com.springroll.core.SpringrollSecurity;
import com.springroll.orm.entities.ReviewStep;
import com.springroll.orm.entities.ReviewRules;
import com.springroll.orm.repositories.ReviewStepRepository;
import com.springroll.orm.repositories.ReviewRulesRepository;
import com.springroll.router.SpringrollEndPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anishjoseph on 27/09/16.
 */
@Service
public class ReviewManager extends SpringrollEndPoint {
    @Autowired
    ApplicationEventPublisher publisher;

    @Autowired
    ReviewRulesRepository reviewRulesRepository;

    @Autowired
    ReviewStepRepository reviewStepRepository;

    public void on(ReviewNeededEvent reviewNeededEvent){
        createReviewSteps(reviewNeededEvent.getPayload().getReviewNeededViolations(), reviewNeededEvent.getPayload().getEventForReview().getJobId(), reviewNeededEvent);
        List<ReviewStep> nextReviewSteps = findNextReviewStep(reviewNeededEvent.getPayload().getEventForReview().getJobId(), 0);
        System.out.println(nextReviewSteps);
    }

    private void createReviewSteps(List<BusinessValidationResult> reviewNeededViolations, Long jobId, ReviewNeededEvent reviewNeededEvent){
        List<ReviewRules> reviewRules = new ArrayList<>();
        for (BusinessValidationResult businessValidationResult : reviewNeededViolations) {
            reviewRules.addAll(reviewRulesRepository.findByRuleName(businessValidationResult.getViolatedRule()));
        }
        List<ReviewStep> reviewSteps = new ArrayList<>();
        for (ReviewRules reviewRule : reviewRules) {
            ReviewStep reviewStep = new ReviewStep(reviewRule.getID(), reviewRule.getReviewStage(), jobId);
            reviewSteps.add(reviewStep);
        }
        reviewSteps.get(0).setEvent(reviewNeededEvent.getPayload().getEventForReview());
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

    public void createReviewNotifications(List<ReviewStep> reviewSteps){
        for (ReviewStep reviewStep : reviewSteps) {

        }
    }

    public void on(ReviewActionEvent reviewActionEvent){
        //FIXME - handle missing review step
        User user = SpringrollSecurity.getUser();
        ReviewActionDTO reviewActionDTO = reviewActionEvent.getPayload();
        ReviewStep reviewStep = reviewStepRepository.findOne(reviewActionDTO.getReviewStepId());
        reviewStep.setReviewedBy(user.getUsername());
        reviewStep.setCompleted(true);
        //FIXME - notify the Notificataion Manager that the step is complete
        List<ReviewStep> reviewSteps = findNextReviewStep(reviewStep.getParentId(), reviewStep.getReviewStage());
        if(reviewSteps.isEmpty()){
            // All reviews are complete
            ReviewStep step = reviewStepRepository.findByParentIdAndSerializedEventIsNotNull(reviewStep.getParentId());
            // The context at this point is that of the user that approved
            // howeever as we push the event that was under review back into
            // the system we need to change the context to that event
            ContextStore.put(step.getEvent().getUser(), step.getEvent().getJobId(), step.getEvent().getLegId());
            publisher.publishEvent(step.getEvent());
            route(step.getEvent());
        }
        createReviewNotifications(reviewSteps);
    }
}
