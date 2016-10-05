package com.springroll.orm.entities;


import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by anishjoseph on 05/09/16.
 */

@Entity
@Table(name = "USERS")
//@NamedQueries(value = {
//        @NamedQuery(name = "Users.findUsersThatBelongToGroup", query = "select user.userId from Users as user where user.grps like ?1")
//})
public class Users extends AbstractEntity {

    private transient Collection<String> groups;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "GROUPS")
    private String grps;

    public Collection<String> getGroups() {
        if(groups == null)groups = StringUtils.commaDelimitedListToSet(grps);
        return groups;
    }

    public void setGroups(Collection<String> grpList) {
        this.grps = StringUtils.collectionToCommaDelimitedString(grpList);
        this.groups = grpList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
