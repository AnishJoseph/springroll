package com.springroll.router.review;

import com.springroll.core.*;
import com.springroll.core.services.INotificationManager;
import com.springroll.notification.CoreNotificationChannels;
import com.springroll.notification.FyiReviewNotificationMessage;
import com.springroll.notification.ReviewNotificationMessage;
import com.springroll.orm.entities.Job;
import com.springroll.orm.entities.ReviewStep;
import com.springroll.orm.entities.ReviewRule;
import com.springroll.orm.repositories.Repositories;
import com.springroll.router.SpringrollEndPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by anishjoseph on 27/09/16.
 * Order of review is
 * SELF first
 * Based on ReviewRule order
 * Last is any rules marked as FYI only in ReviewRule
 *
 */
@Service
public class ReviewManager extends SpringrollEndPoint {
    private static final Logger logger = LoggerFactory.getLogger(ReviewManager.class);

    @Autowired
    ApplicationEventPublisher publisher;

    @Autowired
    Repositories repo;

    @Autowired
    INotificationManager notificationManager;

    public void on(ReviewNeededEvent reviewNeededEvent){
        createReviewSteps(reviewNeededEvent.getPayload().getReviewNeededViolations(), reviewNeededEvent.getPayload().getEventForReview().getJobId(), reviewNeededEvent);
        List<ReviewStep> nextReviewSteps = findNextReviewStep(reviewNeededEvent.getPayload().getEventForReview().getJobId(), -1);
        createReviewNotifications(nextReviewSteps);
    }

    private void createReviewSteps(List<BusinessValidationResult> reviewNeededViolations, Long jobId, ReviewNeededEvent reviewNeededEvent){
        List<ReviewRule> reviewRules = new ArrayList<>();
        for (BusinessValidationResult businessValidationResult : reviewNeededViolations) {
            if("SELF".equals(businessValidationResult.getApprover()))continue;
            reviewRules.addAll(repo.reviewRules.findByRuleNameAndFyiOnly(businessValidationResult.getViolatedRule(), false));
        }
        List<ReviewStep> reviewSteps = new ArrayList<>();
        for (ReviewRule reviewRule : reviewRules) {
            ReviewStep reviewStep = new ReviewStep(reviewRule.getID(), reviewRule.getReviewStage(), jobId, reviewRule.getApprover());
            reviewSteps.add(reviewStep);
        }
        /* Add a step with stage set to 0 for any SELF reviews */
        for (BusinessValidationResult businessValidationResult : reviewNeededViolations) {
            if(!"SELF".equals(businessValidationResult.getApprover()))continue;
            reviewSteps.add(new ReviewStep(-1l, 0, jobId, SpringrollSecurity.getUser().getUsername()));
        }

        /* Store the event under review so that we can send it out after all the reviews are done.
           we need to store this in only one place - keep it in the 0th step
        */
        reviewSteps.get(0).setEvent(reviewNeededEvent.getPayload().getEventForReview());
        reviewSteps.get(0).setBusinessViolations(reviewNeededViolations);
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
        Map<String, List<Long>> approverToNoti = new HashMap<>();
        for (ReviewStep reviewStep : reviewSteps) {
            /* Create the notification payload, send it down the REVIEW channel, and store the review id returned in the step */
            if(!approverToNoti.containsKey(reviewStep.getApprover()))approverToNoti.put(reviewStep.getApprover(), new ArrayList<>());
            approverToNoti.get(reviewStep.getApprover()).add(reviewStep.getID());
        }
        for (String approver : approverToNoti.keySet()) {
            Long notiId = notificationManager.sendNotification(CoreNotificationChannels.REVIEW, new ReviewNotificationMessage(approverToNoti.get(approver), approver));
            for (Long stepId : approverToNoti.get(approver)) {
                repo.reviewStep.findOne(stepId).setNotificationId(notiId);
            }
        }
    }

    public void on(ReviewActionEvent reviewActionEvent) {
        ReviewActionDTO reviewActionDTO = reviewActionEvent.getPayload();
        if(reviewActionDTO.getReviewStepId() == null){
            logger.error("Review Step ID is null");
            return;
        }
        //FIXME - can we parallelize here
        for (Long reviewStepId : reviewActionDTO.getReviewStepId()) {
            actOnOneStep(reviewStepId, reviewActionDTO.isApproved());
        }
    }

    public void actOnOneStep(Long reviewStepId, boolean isApproved){
        ReviewStep reviewStep = repo.reviewStep.findOne(reviewStepId);
        if(reviewStep == null){
            logger.error("Unable to find a review step with id {} - returning silently", reviewStepId);
            return;
        }
        List<ReviewStep> earlierUncompletedSteps = repo.reviewStep.findByCompletedIsFalseAndParentIdAndReviewStageIsLessThan(reviewStep.getParentId(), reviewStep.getReviewStage());
        if(!earlierUncompletedSteps.isEmpty()){
            logger.error("User '{}' is trying to approve/reject a step that is not yet ready for approval", SpringrollSecurity.getUser().getUsername());
            return;
        }
        if(reviewStep.hasThisUserAlreadyReviewedThisStep(SpringrollSecurity.getUser().getUsername())){
            logger.error("User '{}' has already reviewed step '{}'",SpringrollSecurity.getUser().getUsername(), reviewStepId);
            return;
        }

        reviewStep.addReviewLog(new ReviewLog(SpringrollSecurity.getUser().getUsername(), LocalDateTime.now(), isApproved));
        if(reviewStep.getRuleId() != -1) {
            ReviewRule reviewRule = repo.reviewRules.findOne(reviewStep.getRuleId());
            notificationManager.addNotificationAcknowledgement(reviewStep.getNotificationId());
            if (isApproved && reviewRule.getApprovalsNeeded() > reviewStep.getReviewLog().size()) {
            /* Oh this rule requires more that one approval for this stage - nothing to do */
                return;
            }
        }
        reviewStep.setCompleted(true);
        notificationManager.deleteNotification(reviewStep.getNotificationId());

        List<ReviewStep> reviewSteps = findNextReviewStep(reviewStep.getParentId(), reviewStep.getReviewStage());
        if(reviewSteps.isEmpty() || !isApproved){
            // All reviews are complete or someone has rejected this
            List<ReviewStep> allReviewSteps = repo.reviewStep.findByParentId(reviewStep.getParentId());
            List<ReviewLog> reviewLog = new ArrayList<>();
            for (ReviewStep rs : allReviewSteps) {
                reviewLog.addAll(rs.getReviewLog());
            }
            Job job = repo.job.findOne(reviewStep.getParentId());
            job.setReviewLog(reviewLog);
            job.setUnderReview(false);

            if(isApproved) {
                // find the step with the payload so that we can deserialize it and send it for processing
                ReviewStep step = repo.reviewStep.findByParentIdAndSerializedEventIsNotNull(reviewStep.getParentId());
                IEvent reviewedEvent = step.getEvent();
                if (reviewedEvent instanceof ReviewableEvent) {
                    ((ReviewableEvent) reviewedEvent).setReviewLog(reviewLog);
                    ((ReviewableEvent) reviewedEvent).setApproved(isApproved);
                }
                // The context at this point is that of the user that made the approval.  However as we push the event that was under
                // review back into the system we need to change the context to that event
                ContextStore.put(reviewedEvent.getUser(), reviewedEvent.getJobId(), reviewedEvent.getLegId());
                publisher.publishEvent(step.getEvent());
                route(step.getEvent());

                /* Now that the review is complete and approved, send out  FYI notifications, if any */
                sendFyiNotification(reviewStep.getBusinessViolations());
            } else {
                job.setEndTime(LocalDateTime.now());
                job.setStatus(job.getStatus() + " Review Rejected by " + SpringrollSecurity.getUser().getUsername());
                job.setCompleted(true);
            }
            repo.reviewStep.delete(allReviewSteps);
            return;
        }
        createReviewNotifications(reviewSteps);
    }

    private void sendFyiNotification(List<BusinessValidationResult> reviewNeededViolations){
        /* Build a list of businessValidationResults on a per user basis */
        Map<String, List<BusinessValidationResult>> userToNoti = new HashMap<>();
        for (BusinessValidationResult businessValidationResult : reviewNeededViolations) {
            if("SELF".equals(businessValidationResult.getApprover()))continue;
            List<ReviewRule> reviewRules = repo.reviewRules.findByRuleNameAndFyiOnly(businessValidationResult.getViolatedRule(), true);
            for (ReviewRule reviewRule : reviewRules) {
                String userId = reviewRule.getApprover();
                if(!userToNoti.containsKey(userId))userToNoti.put(userId, new ArrayList<>());
                userToNoti.get(userId).add(businessValidationResult);
            }
        }
        for (String user : userToNoti.keySet()) {
            notificationManager.sendNotification(CoreNotificationChannels.REVIEW_FYI, new FyiReviewNotificationMessage(userToNoti.get(user), user));
        }
    }

}
