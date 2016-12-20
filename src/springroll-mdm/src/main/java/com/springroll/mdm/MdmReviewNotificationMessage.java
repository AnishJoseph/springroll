package com.springroll.mdm;

import com.springroll.core.notification.IReviewMeta;
import com.springroll.notification.AbstractNotificationMessage;

import java.util.List;

/**
 * Created by anishjoseph on 02/10/16.
 */
public class MdmReviewNotificationMessage extends AbstractNotificationMessage {
    private List<Long> reviewStepId;
    private String messageKey = "ui.mdm.review.noti.msg";
    private String[] args = new String[]{};
    private MdmChangesForReview mdmChangesForReview;
    public MdmReviewNotificationMessage(){}

    public MdmReviewNotificationMessage(IReviewMeta reviewMeta, String[] args, MdmChangesForReview mdmChangesForReview) {
        setNotificationReceivers(reviewMeta.getApprover());
        this.reviewStepId = reviewMeta.getReviewStepIds();
        this.args = args;
        this.mdmChangesForReview = mdmChangesForReview;
        setInitiator(reviewMeta.getInitiator());
    }

    public List<Long> getReviewStepId() {
        return reviewStepId;
    }

    public void setReviewStepId(List<Long> reviewStepId) {
        this.reviewStepId = reviewStepId;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }

    public MdmChangesForReview getMdmChangesForReview() {
        return mdmChangesForReview;
    }

    public void setMdmChangesForReview(MdmChangesForReview mdmChangesForReview) {
        this.mdmChangesForReview = mdmChangesForReview;
    }

}
