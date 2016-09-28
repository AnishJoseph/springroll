package com.springroll.orm.entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Created by anishjoseph on 05/09/16.
 */

@Entity
@Table(name = "REVIEW_STEP")
public class ReviewStep extends AbstractEntity {

    @Column(name = "RULE_ID")
    private Long ruleId;

    @Column(name = "REVIEW_STAGE")
    @Min(1)
    @Max(100)
    private int reviewStage;

    @Column(name = "COMPLETED")
    private boolean completed = false;

    @Column(name = "REVIEWED_BY")
    private String reviewedBy;

    public ReviewStep(Long ruleId, int reviewStage, Long parentId) {
        this.ruleId = ruleId;
        this.reviewStage = reviewStage;
        this.setParentId(parentId);
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(String reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public int getReviewStage() {
        return reviewStage;
    }

    public void setReviewStage(int reviewStage) {
        this.reviewStage = reviewStage;
    }
}