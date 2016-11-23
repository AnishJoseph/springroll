package com.springroll.orm.entities;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * Created by anishjoseph on 23/11/16.
 */
@MappedSuperclass
public abstract class MdmEntity extends AbstractEntity{
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
