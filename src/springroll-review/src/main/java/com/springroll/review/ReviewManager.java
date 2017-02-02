package com.springroll.review;

import com.springroll.core.*;
import com.springroll.core.services.notification.*;
import com.springroll.core.services.review.ReviewService;
import com.springroll.notification.CorePushChannels;
import com.springroll.orm.entities.Job;
import com.springroll.orm.entities.ReviewRule;
import com.springroll.orm.entities.ReviewStep;
import com.springroll.orm.entities.ReviewStepMeta;
import com.springroll.orm.repositories.Repositories;
import com.springroll.router.AsynchSideEndPoints;
import com.springroll.router.JobManager;
import com.springroll.router.JobStatusMessage;
import com.springroll.router.SpringrollEndPoint;
import com.springroll.router.review.ReviewNeededEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by anishjoseph on 27/09/16.
 * Order of review is
 * SELF first
 * Based on ReviewRule order
 * Last is any rules marked as FYI only in ReviewRule
 *
 */
@Service
public class ReviewManager extends SpringrollEndPoint implements ReviewService {
    private static final Logger logger = LoggerFactory.getLogger(ReviewManager.class);
    private static int REVIEW_STAGE_FOR_FYI = 1000;
    @Autowired ApplicationEventPublisher publisher;

    @Autowired Repositories repo;

    @Autowired NotificationService notificationService;

    @Autowired JobManager jobManager;

    @Autowired AsynchSideEndPoints asynchEndPoint;
    @Autowired SpringrollUtils springrollUtils;

    public void on(ReviewNeededEvent reviewNeededEvent){
        ReviewStepMeta reviewStepMeta = createReviewSteps(reviewNeededEvent.getPayload().getReviewNeededViolations(), reviewNeededEvent.getPayload().getEventForReview().getJobId(), reviewNeededEvent);
        if(reviewStepMeta == null){
            //FIXME - if there are no review steps we may need to mark the job as InProgress (its likely to be UnderReview at this point )
            route(reviewNeededEvent.getPayload().getEventForReview());
            return;
        }
        Job job = repo.job.findOne(reviewNeededEvent.getPayload().getEventForReview().getJobId());
        Set<String> yetToReview = repo.reviewStep.getReviewers(reviewStepMeta.getID());
        job.setPendingReviewers(String.join(", ", yetToReview));
        List<ReviewStep> nextReviewSteps = findNextReviewStep(reviewStepMeta, -1);
        createReviewNotifications(nextReviewSteps,  null);
    }

    private ReviewStepMeta createReviewSteps(List<BusinessValidationResult> reviewNeededViolations, Long jobId, ReviewNeededEvent reviewNeededEvent){
        List<ReviewStep> reviewSteps = new ArrayList<>();
        for (BusinessValidationResult businessValidationResult : reviewNeededViolations) {
            String approver = businessValidationResult.getApprover();
            List<ReviewRule> reviewRules = repo.reviewRules.findByRuleName(businessValidationResult.getViolatedRule());
            for (ReviewRule reviewRule : reviewRules) {
                if(approver == null) approver = reviewRule.getApprover(); //Dont override the approver if the approver is set by the business rule
                int reviewStage = reviewRule.isFyiOnly() ? REVIEW_STAGE_FOR_FYI : reviewRule.getReviewStage(); //Move all FYI ONLY to the end
                if("SELF".equals(approver) && !reviewRule.isFyiOnly()) reviewStage = 0; /* Ensure that all the self reviews happen first */
                if("SELF".equals(approver)) approver = SpringrollSecurity.getUser().getUsername();
                ReviewStep reviewStep = new ReviewStep(reviewRule.getID(), reviewRule.getChannel(), reviewStage, null, approver, businessValidationResult);
                reviewSteps.add(reviewStep);

                if(reviewRule.isSelfReview() && !approver.equals(SpringrollSecurity.getUser().getUsername())){
                    /* This rule requires that a initiator approval is required before it is sent to other approvers */
                    reviewStep = new ReviewStep(reviewRule.getID(), reviewRule.getChannel(), 0, null, SpringrollSecurity.getUser().getUsername(), businessValidationResult);
                    reviewSteps.add(reviewStep);
                }
            }
        }

        /* Store the event under review in reviewStepMeta so that we can send it out after all the reviews are done.
        */
        if(reviewSteps.isEmpty())return null;
        ReviewStepMeta reviewStepMeta = new ReviewStepMeta();
        reviewStepMeta.setEvent(reviewNeededEvent.getPayload().getEventForReview());
        reviewStepMeta.setParentId(jobId);
        reviewStepMeta.setInitiator(reviewNeededEvent.getUser().getUsername());
        if(reviewNeededEvent.getPayload().getEventForReview().getPayload() instanceof Searchable) {
            reviewStepMeta.setSearchId(((Searchable) reviewNeededEvent.getPayload().getEventForReview().getPayload()).getSearchId());
        }
        repo.reviewStepMeta.save(reviewStepMeta);
        for (ReviewStep reviewStep : reviewSteps) {
            reviewStep.setParentId(reviewStepMeta.getID());
        }
        repo.reviewStep.save(reviewSteps);
        return reviewStepMeta;
    }
    private List<ReviewStep> findNextReviewStep(ReviewStepMeta reviewStepMeta, int completedStepId){
        try {
            List<ReviewStep> allFutureSteps = repo.reviewStep.findByParentIdAndReviewStageIsGreaterThan(reviewStepMeta.getID(), completedStepId);
            int minReviewStage = 10000;
            for (ReviewStep futureStep : allFutureSteps) {
                if (futureStep.getReviewStage() < minReviewStage) minReviewStage = futureStep.getReviewStage();
            }
            List<ReviewStep> nextSteps = repo.reviewStep.findByParentIdAndReviewStage(reviewStepMeta.getID(), minReviewStage);
            return nextSteps;
        }catch (Exception e){
            e.printStackTrace();
            throw  new RuntimeException(e);
        }
    }

    private void createReviewNotifications(List<ReviewStep> reviewSteps, List<ReviewLog> reviewLog){
        ReviewStepMeta reviewStepMeta = repo.reviewStepMeta.findOne(reviewSteps.get(0).getParentId());

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
            NotificationChannel notificationChannel = notificationService.nameToEnum(step.getChannel());

            String serviceName = ((ServiceDTO)reviewStepMeta.getEvent().getPayload()).getServiceDefinition().name();
            INotificationMessage message = ((IReviewableNotificationMessageFactory)notificationChannel.getMessageFactory()).makeMessage(new ReviewMeta(approverToNoti.get(approver), approver, businessValidationResults, reviewStepMeta.getEvent().getUser().getUsername(), serviceName, reviewLog, reviewStepMeta.getEvent().getPayloads()));
            Long notiId = notificationService.sendNotification(notificationChannel, message);
            for (Long stepId : approverToNoti.get(approver)) {
                repo.reviewStep.findOne(stepId).setNotificationId(notiId);
            }
        }
    }

    private boolean isValid(ReviewStep reviewStep, Long reviewStepId){
        if (reviewStep == null) {
            logger.error("Unable to find a review step with id {} - returning silently", reviewStepId);
            return false;
        }
        if (reviewStep.isCompleted()) {
            /* This step is already been reviewed and needs no further review */
            return false;
        }
        List<ReviewStep> earlierUncompletedSteps = repo.reviewStep.findByCompletedIsFalseAndParentIdAndReviewStageIsLessThan(reviewStep.getParentId(), reviewStep.getReviewStage());
        if (!earlierUncompletedSteps.isEmpty()) {
            logger.error("User '{}' is trying to approve/reject a step that is not yet ready for approval", SpringrollSecurity.getUser().getUsername());
            return false;
        }
        if (reviewStep.hasThisUserAlreadyReviewedThisStep(SpringrollSecurity.getUser().getUsername())) {
            logger.error("User '{}' has already reviewed step '{}'", SpringrollSecurity.getUser().getUsername(), reviewStepId);
            return false;
        }
        return true;

    }

    private boolean markStepCompleteAndDetermineIfStepIsComplete(ReviewStep reviewStep, ReviewActionDTO reviewActionDTO){
        reviewStep.addReviewLog(new ReviewLog(SpringrollSecurity.getUser().getUsername(), LocalDateTime.now(), reviewActionDTO.isApproved(), reviewActionDTO.getReviewComment()));
        ReviewRule reviewRule = repo.reviewRules.findOne(reviewStep.getRuleId());
        if (reviewStep.getReviewStage() != 0 && reviewActionDTO.isApproved() && reviewRule.getApprovalsNeeded() > reviewStep.getReviewLog().size()) {
            /* Oh this rule requires multiple approvals for this stage - this step is not yet complete */
            return false;
        }
        reviewStep.setCompleted(true);
        return true;
    }

    public void on(ReviewActionEvent reviewActionEvent) {
        ReviewActionDTO reviewActionDTO = reviewActionEvent.getPayload();
        if (reviewActionDTO.getReviewStepId() == null) {
            logger.error("Review Step ID is null");
            return;
        }
        boolean areAllStepsComplete = true;
        ReviewStep reviewStep = null;
        Set<Long> notificationIds = new HashSet<>();
        for (Long reviewStepId : reviewActionDTO.getReviewStepId()) {
            reviewStep = repo.reviewStep.findOne(reviewStepId);
            if (!isValid(reviewStep, reviewStepId)) continue;
            boolean isThisStepComplete = markStepCompleteAndDetermineIfStepIsComplete(reviewStep, reviewActionDTO);
            areAllStepsComplete =   isThisStepComplete && areAllStepsComplete;
            notificationIds.add(reviewStep.getNotificationId());
            notificationService.addNotificationAcknowledgement(reviewStep.getNotificationId());
        }
        ReviewStepMeta reviewStepMeta = repo.reviewStepMeta.findOne(reviewStep.getParentId());
        Job job = repo.job.findOne(reviewStepMeta.getParentId());
        Set<String> yetToReview = repo.reviewStep.getReviewers(reviewStepMeta.getID());
        job.setPendingReviewers(String.join(", ", yetToReview));

        job.addReviewLog(new ReviewLog(SpringrollSecurity.getUser().getUsername(), LocalDateTime.now(), reviewActionDTO.isApproved(), reviewActionDTO.getReviewComment()));
        Set<String> users = new HashSet<>(1);
        users.add(job.getUserId());
        notificationService.pushNotification(users, new JobStatusMessage(job), CorePushChannels.JOB_STATUS_UPDATE);
        if(!areAllStepsComplete) return;

        for (Long notificationId : notificationIds) {
            notificationService.deleteNotification(notificationId);
        }

        /* All the steps reviewed (for this stage) are complete - move to the next step if present, else emit the event that was under review */
        List<ReviewStep> reviewSteps = findNextReviewStep(reviewStepMeta, reviewStep.getReviewStage());
        List<ReviewStep> allReviewSteps = repo.reviewStep.findByParentId(reviewStep.getParentId());
        if (reviewSteps.isEmpty() || !reviewActionDTO.isApproved() || reviewSteps.get(0).getReviewStage() == REVIEW_STAGE_FOR_FYI) {
            // All reviews are complete or someone has rejected this or the remaining steps are just FYI
            if(reviewActionDTO.isApproved()){
                job.setJobStatus(JobStatus.InProgress);
            } else {
                job.setJobStatus(JobStatus.RejectedAndForwarded);
            }

            IEvent reviewedEvent = reviewStepMeta.getEvent();
            if (reviewedEvent instanceof ReviewableEvent || reviewActionDTO.isApproved()) {
                // find the step with the payload so that we can deserialize it and send it for processing
                if (reviewedEvent instanceof ReviewableEvent) {
                    ((ReviewableEvent) reviewedEvent).setReviewLog(job.getReviewLog());
                    ((ReviewableEvent) reviewedEvent).setApproved(reviewActionDTO.isApproved());
                }
                /*  The event is now fully reviewed - send the event down the JMS bus so that the reviewed
                    event will be evaluated in its own thread and context (separate from this thread which
                    is handling the review approval.
                 */
                asynchEndPoint.routeToJms(reviewStepMeta.getEvent());
                /* Now that the review is complete and approved, send out FYI notifications, if any */
                if (!reviewSteps.isEmpty()) sendFyiNotification(reviewSteps);
            } else {
                /* This is not a ReviewableEvent AND it has been rejected */
                job.setEndTime(LocalDateTime.now());
                String prevStatus = job.getStatus() == null ? "" : job.getStatus();
                job.setStatus(prevStatus + " Review Rejected by " + SpringrollSecurity.getUser().getUsername());
                job.setCompleted(true);
                job.setJobStatus(JobStatus.RejectedAndDiscarded);
                Set<String> targetUser = new HashSet<>(1);
                users.add(job.getUserId());
                notificationService.pushNotification(targetUser, new JobStatusMessage(job), CorePushChannels.JOB_STATUS_UPDATE);
            }
            repo.reviewStep.delete(allReviewSteps);
            repo.reviewStepMeta.delete(reviewStepMeta.getID());
            return;
        }
        createReviewNotifications(reviewSteps, job.getReviewLog());
    }

    private void sendFyiNotification(List<ReviewStep> reviewSteps){
        ReviewStepMeta reviewStepMeta = repo.reviewStepMeta.findOne(reviewSteps.get(0).getParentId());
        Job job = repo.job.findOne(reviewStepMeta.getParentId());

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
            NotificationChannel notificationChannel = notificationService.nameToEnum(step.getChannel());
            String serviceName = ((ServiceDTO)reviewStepMeta.getEvent().getPayload()).getServiceDefinition().name();
            INotificationMessage message = ((IReviewableNotificationMessageFactory)notificationChannel.getMessageFactory()).makeMessage(new ReviewMeta(approverToNoti.get(approver), approver,
                    businessValidationResults, reviewStepMeta.getEvent().getUser().getUsername(), serviceName, job.getReviewLog(), reviewStepMeta.getEvent().getPayloads()));
            notificationService.sendNotification(notificationChannel, message);
        }
    }

    @Override
    public String getServiceInstanceForReviewId(boolean approved, Long reviewStepId) {
        ReviewStep reviewStep = repo.reviewStep.findOne(reviewStepId);
        ReviewStepMeta reviewStepMeta = repo.reviewStepMeta.findOne(reviewStep.getParentId());
        Job job = repo.job.findOne(reviewStepMeta.getParentId());
        if(approved){
            return LocaleFactory.getLocalizedMessage(SpringrollSecurity.getUser().getLocale(), "serviceinstance.review.approve", job.getService(), job.getServiceDescription(), job.getUserId())  + " at " + job.getStartTime().format(springrollUtils.getDateTimeFormatter());
        }
        return LocaleFactory.getLocalizedMessage(SpringrollSecurity.getUser().getLocale(), "serviceinstance.review.reject", job.getService(), job.getServiceDescription(), job.getUserId()) + " at " + job.getStartTime().format(springrollUtils.getDateTimeFormatter());
    }

    private class ReviewMeta implements IReviewMeta {
        private List<Long> reviewStepIds;
        private String approver;
        private List<BusinessValidationResult> businessValidationResults;
        private String initiator;
        private String service;
        private List<ReviewLog> reviewLogs;
        private List<DTO> dtosUnderReview;

        public ReviewMeta(List<Long> reviewStepIds, String approver, List<BusinessValidationResult> businessValidationResults, String initiator, String service, List<ReviewLog> reviewLogs, List<DTO> dtosUnderReview) {
            this.reviewStepIds = reviewStepIds;
            this.approver = approver;
            this.businessValidationResults = businessValidationResults;
            this.initiator = initiator;
            this.service = service;
            this.reviewLogs = reviewLogs;
            this.dtosUnderReview = dtosUnderReview;
        }

        @Override
        public List<Long> getReviewStepIds() {
            return reviewStepIds;
        }

        @Override
        public String getApprover() {
            return approver;
        }

        @Override
        public List<BusinessValidationResult> getBusinessValidationResults() {
            return businessValidationResults;
        }

        @Override
        public String getInitiator() {
            return initiator;
        }

        @Override
        public String getService() {
            return service;
        }

        @Override
        public List<ReviewLog> getReviewLogs() {
            return reviewLogs;
        }

        @Override
        public List<DTO> getDtosUnderReview() {
            return dtosUnderReview;
        }
    }

}
