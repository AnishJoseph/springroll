package com.springroll.orm.entities;


import javax.persistence.*;

/**
 * Created by anishjoseph on 05/09/16.
 */
@NamedQueries({
        @NamedQuery(name="ReviewRule.getRecordsForMdm", query = "SELECT o.id, o.ruleName, o.channel, o.reviewStage, o.approver, o.approvalsNeeded, o.fyiOnly, o.description FROM ReviewRule o")
})

@Entity
@Table(name = "Review_Rule")
public class ReviewRule extends AbstractEntity {

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

    @Column(name = "FYI_ONLY")
    private Boolean fyiOnly = false;

    @Column(name = "DESCRIPTION")
    private String description;

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

    public Boolean isFyiOnly() {
        return fyiOnly;
    }

    public void setFyiOnly(Boolean fyiOnly) {
        this.fyiOnly = fyiOnly;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}