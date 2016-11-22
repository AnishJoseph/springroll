package com.springroll.orm.entities;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anishjoseph on 05/09/16.
 */
@NamedQueries({
        @NamedQuery(name="ReviewRule.getRecordsForMdm",
                query = "SELECT o.id, o.ruleName, o.channel, o.reviewStage, o.approver, o.approvalsNeeded, o.fyiOnly, o.description, o.active FROM ReviewRule o where o.id not  in :idsUnderReview order by o.id"),
        @NamedQuery(name="ReviewRule.getRecordsForMdmForIds",
                query = "SELECT o.id, o.ruleName, o.channel, o.reviewStage, o.approver, o.approvalsNeeded, o.fyiOnly, o.description, o.active FROM ReviewRule o  where o.id in :ids order by o.id")
})

@Entity
@Table(name = "Review_Rule")
public class ReviewRule extends AbstractEntity implements IMdmEntity{

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
    private boolean fyiOnly = false;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ACTIVE", length = 1)
    @NotNull
    private boolean active;

    @ElementCollection
    @CollectionTable(name = "REVIEWS", joinColumns=@JoinColumn(name="PARENT_ID"))
    private List<Reviews> reviewLogs = new ArrayList<>();


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

    public List<Reviews> getReviewLogs() {
        return reviewLogs;
    }

    public void setReviewLogs(List<Reviews> reviewLogs) {
        this.reviewLogs = reviewLogs;
    }
}