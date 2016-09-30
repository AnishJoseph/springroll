package com.springroll.orm.entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by anishjoseph on 05/09/16.
 */

@Entity
@Table(name = "Review_Rules")
public class ReviewRules extends AbstractEntity {

    @Column(name = "RULE_NAME")
    private String ruleName;

    @Column(name = "REVIEW_STAGE")
    private int reviewStage;

    @Column(name = "APPROVER")
    private String approver;

    @Column(name = "NUMBER_OF_APPROVALS_NEEDED")
    private int numberOfApprovalsNeeded;

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

    public int getNumberOfApprovalsNeeded() {
        return numberOfApprovalsNeeded;
    }

    public void setNumberOfApprovalsNeeded(int numberOfApprovalsNeeded) {
        this.numberOfApprovalsNeeded = numberOfApprovalsNeeded;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}