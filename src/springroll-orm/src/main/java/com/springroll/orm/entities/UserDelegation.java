package com.springroll.orm.entities;

import org.hibernate.annotations.Type;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by anishjoseph on 17/10/16.
 */
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

    @Column(name = "START_DATE")
    @Type(type="com.springroll.orm.LocalDateTimeUserType")
    private LocalDate offStartDate;

    @Column(name = "END_DATE")
    @Type(type="com.springroll.orm.LocalDateTimeUserType")
    private LocalDate offEndDate;

    @Column(name = "IS_ACTIVE", length = 1)
    @NotNull
    private boolean isActive;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDate getOffStartDate() {
        return offStartDate;
    }

    public void setOffStartDate(LocalDate offStartDate) {
        this.offStartDate = offStartDate;
    }

    public LocalDate getOffEndDate() {
        return offEndDate;
    }

    public void setOffEndDate(LocalDate offEndDate) {
        this.offEndDate = offEndDate;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

}
