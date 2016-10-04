package com.springroll.router.review;

import com.springroll.core.*;
import com.springroll.core.services.INotificationManager;
import com.springroll.core.services.IReviewManager;
import com.springroll.notification.InternalNotificationChannels;
import com.springroll.orm.entities.Job;
import com.springroll.orm.entities.ReviewStep;
import com.springroll.orm.entities.ReviewRules;
import com.springroll.orm.repositories.Repositories;
import com.springroll.router.SpringrollEndPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anishjoseph on 27/09/16.
 */
@Service
public class ReviewManager extends SpringrollEndPoint implements IReviewManager {
    private static final Logger logger = LoggerFactory.getLogger(ReviewManager.class);

    @Autowired
    ApplicationEventPublisher publisher;

    @Autowired
    Repositories repo;

    @Autowired
    INotificationManager notificationManager;

    public void on(ReviewNeededEvent reviewNeededEvent){
        createReviewSteps(reviewNeededEvent.getPayload().getReviewNeededViolations(), reviewNeededEvent.getPayload().getEventForReview().getJobId(), reviewNeededEvent);
        List<ReviewStep> nextReviewSteps = findNextReviewStep(reviewNeededEvent.getPayload().getEventForReview().getJobId(), 0);
        createReviewNotifications(nextReviewSteps);
    }

    private void createReviewSteps(List<BusinessValidationResult> reviewNeededViolations, Long jobId, ReviewNeededEvent reviewNeededEvent){
        List<ReviewRules> reviewRules = new ArrayList<>();
        for (BusinessValidationResult businessValidationResult : reviewNeededViolations) {
            reviewRules.addAll(repo.reviewRules.findByRuleName(businessValidationResult.getViolatedRule()));
        }
        List<ReviewStep> reviewSteps = new ArrayList<>();
        for (ReviewRules reviewRule : reviewRules) {
            ReviewStep reviewStep = new ReviewStep(reviewRule.getID(), reviewRule.getReviewStage(), jobId);
            reviewSteps.add(reviewStep);
        }
        reviewSteps.get(0).setEvent(reviewNeededEvent.getPayload().getEventForReview());
        repo.reviewStep.save(reviewSteps);
    }
    private List<ReviewStep> findNextReviewStep(Long jobId, int completedStepId){
        List<ReviewStep> allFutureSteps = repo.reviewStep.findByParentIdAndReviewStageIsGreaterThan(jobId, completedStepId);
        int minReviewStage = 10000;
        for (ReviewStep futureStep : allFutureSteps) {
            if(futureStep.getReviewStage() < minReviewStage)minReviewStage = futureStep.getReviewStage();
        }
        List<ReviewStep> nextSteps = repo.reviewStep.findByParentIdAndReviewStage(jobId, minReviewStage);
        return nextSteps;
    }

    public void createReviewNotifications(List<ReviewStep> reviewSteps){
        for (ReviewStep reviewStep : reviewSteps) {
            /* Create the notification payload, send it down the REVIEW channel, and store the review id returned in the step */
            reviewStep.setNotificationId(notificationManager.sendNotification(InternalNotificationChannels.REVIEW, new ReviewNotificationPayload(reviewStep.getID()), true, true));
        }
    }

    public void on(ReviewActionEvent reviewActionEvent){
        ReviewActionDTO reviewActionDTO = reviewActionEvent.getPayload();
        ReviewStep reviewStep = repo.reviewStep.findOne(reviewActionDTO.getReviewStepId());
        if(reviewStep == null){
            logger.error("Unable to find a review step with id {} - returning silently", reviewActionDTO.getReviewStepId());
            return;
        }
        List<ReviewStep> earlierUncompletedSteps = repo.reviewStep.findByCompletedIsFalseAndParentIdAndReviewStageIsLessThan(reviewStep.getParentId(), reviewStep.getReviewStage());
        if(!earlierUncompletedSteps.isEmpty()){
            logger.error("User '{}' is trying to approve/reject a step that is not yet ready for approval", reviewActionEvent.getUser().getUsername());
            return;
        }
        if(reviewStep.hasThisUserAlreadyReviewedThisStep(reviewActionEvent.getUser().getUsername())){
            logger.error("User '{}' has already reviewed step '{}'",reviewActionEvent.getUser().getUsername(), reviewActionDTO.getReviewStepId());
            return;
        }

        reviewStep.addReviewData(new ReviewLog(SpringrollSecurity.getUser().getUsername(), LocalDateTime.now(), reviewActionEvent.getPayload().isApproved()));
        ReviewRules reviewRule = repo.reviewRules.findOne(reviewStep.getRuleId());
        if(reviewActionDTO.isApproved() && reviewRule.getApprovalsNeeded() > reviewStep.getReviewLog().size()){
            /* Oh this rule requires more that one approval for this stage - nothing to do */
            return;
        }
        reviewStep.setCompleted(true);
        notificationManager.deleteNotification(reviewStep.getNotificationId());

        List<ReviewStep> reviewSteps = findNextReviewStep(reviewStep.getParentId(), reviewStep.getReviewStage());
        if(reviewSteps.isEmpty() || !reviewActionDTO.isApproved()){
            // All reviews are complete or someone has rejected this
            List<ReviewStep> allReviewSteps = repo.reviewStep.findByParentId(reviewStep.getParentId());
            List<ReviewLog> reviewLog = new ArrayList<>();
            for (ReviewStep rs : allReviewSteps) {
                reviewLog.addAll(rs.getReviewLog());
            }
            Job job = repo.job.findOne(reviewStep.getParentId());
            job.setReviewLog(reviewLog);
            job.setUnderReview(false);

            if(reviewActionDTO.isApproved()) {
                // find the step with the payload so that we can deserialize it and send it for processing
                ReviewStep step = repo.reviewStep.findByParentIdAndSerializedEventIsNotNull(reviewStep.getParentId());
                IEvent reviewedEvent = step.getEvent();
                if (reviewedEvent instanceof ReviewableEvent) {
                    ((ReviewableEvent) reviewedEvent).setReviewLog(reviewLog);
                    ((ReviewableEvent) reviewedEvent).setApproved(reviewActionEvent.getPayload().isApproved());
                }
                // The context at this point is that of the user that made the approval.  However as we push the event that was under
                // review back into the system we need to change the context to that event
                ContextStore.put(reviewedEvent.getUser(), reviewedEvent.getJobId(), reviewedEvent.getLegId());
                publisher.publishEvent(step.getEvent());
                route(step.getEvent());
            } else {
                job.setEndTime(LocalDateTime.now());
                job.setStatus(job.getStatus() + " Review Rejected by " + reviewActionEvent.getUser().getUsername());
                job.setCompleted(true);
            }
            repo.reviewStep.delete(allReviewSteps);
            return;
        }
        createReviewNotifications(reviewSteps);
    }

    /**
     *
     * @param reviewStepId - id of the reviewStep
     * @return null if the review step does not exist - else the group name of the group that is allowed to review this step
     */
    @Override
    public String getApproverForReviewStep(Long reviewStepId) {
        ReviewStep reviewStep = repo.reviewStep.findOne(reviewStepId);
        if(reviewStep == null)return null;
        ReviewRules reviewRule = repo.reviewRules.findOne(reviewStep.getRuleId());
        return reviewRule.getApprover();
    }
}
