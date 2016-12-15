package com.springroll.orm.entities;


import com.springroll.orm.CSVListConverter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

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


    @Column(name = "USER_ID")
    @NotNull
    @Size(max = 32, min = 1)
    private String userId;

    @NotNull
    @Size(min = 1)
    @Column(name = "ROLES")
    @Convert(converter = CSVListConverter.class)
    private List<String> roles;

    @Column(name = "COUNTRY")
    private String country;

    @Column(name = "LANGUAGE")
    private String language;

    @Column(name = "VARIANT")
    private String variant;

    @Column(name = "ACTIVE")
    @NotNull
    private boolean active;


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

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
