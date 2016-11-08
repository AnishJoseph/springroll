package com.springroll.orm.entities;

import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Configurable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Created by anishjoseph on 17/10/16.
 */

@NamedQueries({
        @NamedQuery(name="UserDelegation.findDelegators", query = "SELECT o.userId FROM UserDelegation o WHERE o.delegate = ?1 and ?2 BETWEEN o.startDate AND o.endDate"),
        @NamedQuery(name="UserDelegation.isValidDelegate", query = "SELECT o.userId FROM UserDelegation o WHERE o.delegate = ?1 and ?2 = o.userId and ?3 BETWEEN o.startDate AND o.endDate"),
        @NamedQuery(name="UserDelegation.getRecordsForMdm", query = "SELECT o.id, o.userId, o.delegate, o.startDate, o.endDate, o.description, o.active FROM UserDelegation o")
})
@Configurable
@Entity
@Table(name = "USER_DELEGATION", uniqueConstraints = { @UniqueConstraint(name = "I_USER_DELEGATION", columnNames = {"USER_ID", "START_DATE" }) })

public class UserDelegation extends AbstractEntity {

    private static final long serialVersionUID = 1l;

    @Column(name = "USER_ID")
    @NotNull
    private String userId;

    @Column(name = "DELEGATE")
    @NotNull
    private String delegate;

    //FIXME - make this a date - conver to hiberate 5
    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @Column(name = "ACTIVE", length = 1)
    @NotNull
    private boolean active;

    @Column(name = "DESCRIPTION", length = 256)
    private String description;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDelegate() {
        return delegate;
    }

    public void setDelegate(String delegate) {
        this.delegate = delegate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
