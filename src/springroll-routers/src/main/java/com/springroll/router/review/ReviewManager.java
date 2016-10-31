package com.springroll.router.review;

import com.springroll.core.*;
import com.springroll.core.notification.INotificationChannel;
import com.springroll.core.notification.INotificationMessage;
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
    private static int REVIEW_STAGE_FOR_FYI = 1000;
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
        List<ReviewStep> reviewSteps = new ArrayList<>();
        for (BusinessValidationResult businessValidationResult : reviewNeededViolations) {
            String approver = businessValidationResult.getApprover();
            List<ReviewRule> reviewRules = repo.reviewRules.findByRuleName(businessValidationResult.getViolatedRule());
            for (ReviewRule reviewRule : reviewRules) {
                if(approver == null) approver = reviewRule.getApprover(); //Dont override the approver if the approver is set by the business rule
                int reviewStage = reviewRule.isFyiOnly() ? REVIEW_STAGE_FOR_FYI : reviewRule.getReviewStage(); //Move all FYI ONLY to the end
                if("SELF".equals(approver) && !reviewRule.isFyiOnly()) reviewStage = 0; /* Ensure that all the self reviews happen first */
                if("SELF".equals(approver)) approver = SpringrollSecurity.getUser().getUsername();
                ReviewStep reviewStep = new ReviewStep(reviewRule.getID(), reviewRule.getChannel(), reviewStage, jobId, approver, businessValidationResult);
                reviewSteps.add(reviewStep);
            }
        }

        /* Store the event under review so that we can send it out after all the reviews are done.
           we need to store this in only one place - keep it in the 0th step
        */
        reviewSteps.get(0).setEvent(reviewNeededEvent.getPayload().getEventForReview());
        repo.reviewStep.save(reviewSteps);
    }
    private List<ReviewStep> findNextReviewStep(Long jobId, int completedStepId){
        try {
            List<ReviewStep> allFutureSteps = repo.reviewStep.findByParentIdAndReviewStageIsGreaterThan(jobId, completedStepId);
            int minReviewStage = 10000;
            for (ReviewStep futureStep : allFutureSteps) {
                if (futureStep.getReviewStage() < minReviewStage) minReviewStage = futureStep.getReviewStage();
            }
            List<ReviewStep> nextSteps = repo.reviewStep.findByParentIdAndReviewStage(jobId, minReviewStage);
            return nextSteps;
        }catch (Exception e){
            e.printStackTrace();
            throw  new RuntimeException(e);
        }
    }

    private void createReviewNotifications(List<ReviewStep> reviewSteps){
        ReviewStep step0 = repo.reviewStep.findByParentIdAndSerializedEventIsNotNull(reviewSteps.get(0).getParentId());
        Map<String, List<Long>> approverToNoti = new HashMap<>();
        for (ReviewStep reviewStep : reviewSteps) {
            /*  If there are multiple reviews steps dont blindly send one notification per review step.
                Try and do a bit of optimization - if multiple steps are targeted at the same approver
                we need to send only one notification (the notification will have the IDs of all the
                review steps that were bunched together.
             */
            if(!approverToNoti.containsKey(reviewStep.getApprover()))approverToNoti.put(reviewStep.getApprover(), new ArrayList<>());
            approverToNoti.get(reviewStep.getApprover()).add(reviewStep.getID());
        }

        for (String approver : approverToNoti.keySet()) {
            //FIXME - BIG ASSUMPTION HERE - we are assuming here that all reviews for a given approver goes to the same channel - this may not be true.
            ReviewStep step = repo.reviewStep.findOne(approverToNoti.get(approver).get(0));
            List<BusinessValidationResult> businessValidationResults = new ArrayList<>();
            for (Long stepId : approverToNoti.get(approver)) {
                businessValidationResults.add(repo.reviewStep.findOne(stepId).getViolationForThisStep());
            }
            /* Create the notification payload, send it down the REVIEW channel, and store the review id returned in the step */
            INotificationChannel notificationChannel = notificationManager.nameToEnum(step.getChannel());

            String serviceName = ((ServiceDTO)step0.getEvent().getPayload()).getProcessor().name();
            INotificationMessage message = notificationChannel.getMessageFactory().makeMessage(approverToNoti.get(approver), approver, businessValidationResults, step0.getEvent().getUser(), serviceName);
            Long notiId = notificationManager.sendNotification(notificationChannel, message);
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

    private void actOnOneStep(Long reviewStepId, boolean isApproved){
        ReviewStep reviewStep = repo.reviewStep.findOne(reviewStepId);
        if(reviewStep == null){
            logger.error("Unable to find a review step with id {} - returning silently", reviewStepId);
            return;
        }
        List<ReviewStep> earlierUncompletedSteps = repo.reviewStep.findByCompletedAndParentIdAndReviewStageIsLessThan(false, reviewStep.getParentId(), reviewStep.getReviewStage());
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
        if(reviewSteps.isEmpty() || !isApproved || reviewSteps.get(0).getReviewStage() == REVIEW_STAGE_FOR_FYI){
            // All reviews are complete or someone has rejected this or the remaining steps are just FYI
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
                if(!reviewSteps.isEmpty())sendFyiNotification(reviewSteps);
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

    private void sendFyiNotification(List<ReviewStep> reviewSteps){
        ReviewStep step0 = repo.reviewStep.findByParentIdAndSerializedEventIsNotNull(reviewSteps.get(0).getParentId());
        Map<String, List<Long>> approverToNoti = new HashMap<>();
        for (ReviewStep reviewStep : reviewSteps) {
            /* Create the notification payload, send it down the REVIEW channel, and store the review id returned in the step */
            if(!approverToNoti.containsKey(reviewStep.getApprover()))approverToNoti.put(reviewStep.getApprover(), new ArrayList<>());
            approverToNoti.get(reviewStep.getApprover()).add(reviewStep.getID());
        }
        for (String approver : approverToNoti.keySet()) {
            ReviewStep step = repo.reviewStep.findOne(approverToNoti.get(approver).get(0));
            List<BusinessValidationResult> businessValidationResults = new ArrayList<>();
            for (Long stepId : approverToNoti.get(approver)) {
                businessValidationResults.add(repo.reviewStep.findOne(stepId).getViolationForThisStep());
            }
            INotificationChannel notificationChannel = notificationManager.nameToEnum(step.getChannel());
            String serviceName = ((ServiceDTO)step0.getEvent().getPayload()).getProcessor().name();
            INotificationMessage message = notificationChannel.getMessageFactory().makeMessage(approverToNoti.get(approver), approver, businessValidationResults, step0.getEvent().getUser(), serviceName);

            notificationManager.sendNotification(notificationChannel, message);
        }
    }

}
