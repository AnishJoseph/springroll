package com.springrollexample.orm.entities;


import com.springroll.orm.entities.AbstractEntity;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Collection;
import java.util.List;

/**
 * Created by anishjoseph on 05/09/16.
 */

@Entity
@Table(name = "GROUPS")
public class Groups extends AbstractEntity {

    private transient Collection<String> auths;

    private String authorities;

    @Column(name = "GROUP", length = 32)
    private String group;

    @Column(name = "DESCRIPTION", length = 256)
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<String> getAuths() {
        if(auths == null)auths = StringUtils.commaDelimitedListToSet(authorities);
        return auths;
    }

    public void setAuths(Collection<String> auths) {
        authorities = StringUtils.collectionToCommaDelimitedString(auths);
        this.auths = auths;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
