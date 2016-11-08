package com.springroll.orm.entities;


import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by anishjoseph on 05/09/16.
 */
@NamedQueries({
        @NamedQuery(name="User.getRecordsForMdm", query = "SELECT o.id, o.userId, o.roles, o.country, o.language, o.variant, o.active FROM User o"),
        @NamedQuery(name="User.getActiveUsers", query = "SELECT o.userId FROM User o where o.active = 'T'")
})

@Entity
@Table(name = "USER")
public class User extends AbstractEntity {

    private transient Collection<String> rolesList;

    @Column(name = "USER_ID")
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
