package com.springroll.orm.entities;


import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by anishjoseph on 05/09/16.
 */

@Entity
@Table(name = "Review_Rule")
@Audited
public class ReviewRule extends MdmEntity{


    @Column(name = "RULE_NAME")
    private String ruleName;

    @Column(name = "CHANNEL")
    private String channel;

    @Column(name = "REVIEW_STAGE")
    private int reviewStage;

    @Column(name = "APPROVER")
    private String approver;

    @Column(name = "APPROVALS_NEEDED")
    private int approvalsNeeded;

    @Column(name = "SELF_REVIEW", length = 1)
    @NotNull
    private boolean selfReview;

    @Column(name = "FYI_ONLY")
    private boolean fyiOnly = false;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ACTIVE", length = 1)
    @NotNull
    private boolean active;

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public int getReviewStage() {
        return reviewStage;
    }

    public void setReviewStage(int reviewStage) {
        this.reviewStage = reviewStage;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public int getApprovalsNeeded() {
        return approvalsNeeded;
    }

    public void setApprovalsNeeded(int approvalsNeeded) {
        this.approvalsNeeded = approvalsNeeded;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFyiOnly() {
        return fyiOnly;
    }

    public void setFyiOnly(boolean fyiOnly) {
        this.fyiOnly = fyiOnly;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isSelfReview() {
        return selfReview;
    }

    public void setSelfReview(boolean selfReview) {
        this.selfReview = selfReview;
    }
}