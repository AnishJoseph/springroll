package com.springroll.mdm;

import com.springroll.core.services.notification.AlertType;
import com.springroll.core.services.notification.IReviewMeta;
import com.springroll.notification.AbstractAlertMessage;

import java.util.List;

/**
 * Created by anishjoseph on 02/10/16.
 */
public class MdmReviewAlertMessage extends AbstractAlertMessage {
    private List<Long> reviewStepId;
    private String messageKey = "ui.mdm.review.noti.msg";
    private String[] args = new String[]{};
    private MdmChangesForReview mdmChangesForReview;
    {
        alertType = AlertType.ACTION;
    }
    public MdmReviewAlertMessage(){
    }

    public MdmReviewAlertMessage(IReviewMeta reviewMeta, String[] args, MdmChangesForReview mdmChangesForReview) {
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
