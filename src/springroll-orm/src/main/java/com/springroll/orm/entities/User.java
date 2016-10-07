package com.springroll.orm.entities;


import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by anishjoseph on 05/09/16.
 */

@Entity
@Table(name = "USER")
public class User extends AbstractEntity {

    private transient Collection<String> rolesList;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "ROLES")
    private String roles;

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
}
