package com.springroll.orm.entities;


import com.springroll.orm.entities.AbstractEntity;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Collection;

/**
 * Created by anishjoseph on 05/09/16.
 */

@Entity
@Table(name = "USERS")
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
