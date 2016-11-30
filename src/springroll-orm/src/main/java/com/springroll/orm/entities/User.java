package com.springroll.orm.entities;


import org.hibernate.envers.Audited;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

/**
 * Created by anishjoseph on 05/09/16.
 */
@NamedQueries({
        @NamedQuery(name="User.getActiveUsers", query = "SELECT o.userId FROM User o where o.active = true")
})

@Entity
@Table(name = "USER")
@Audited
public class User extends MdmEntity {

    @NotNull
    @Size(min = 1)
    private transient Collection<String> rolesList;

    @Column(name = "USER_ID")
    @NotNull
    @Size(max = 32, min = 1)
    private String userId;

    @Column(name = "ROLES")
    private String roles;

    @Column(name = "COUNTRY")
    private String country;

    @Column(name = "LANGUAGE")
    private String language;

    @Column(name = "VARIANT")
    private String variant;

    @Column(name = "ACTIVE")
    @NotNull
    private boolean active;


    public Collection<String> getRoles() {
        if(rolesList == null)rolesList = StringUtils.commaDelimitedListToSet(roles);
        return rolesList;
    }

    public void setRoles(Collection<String> rolesList) {
        this.roles = StringUtils.collectionToCommaDelimitedString(rolesList);
        this.rolesList = rolesList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
