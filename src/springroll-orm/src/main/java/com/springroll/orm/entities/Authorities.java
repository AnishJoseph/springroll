package com.springroll.orm.entities;


import com.springroll.orm.entities.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by anishjoseph on 05/09/16.
 */

@Entity
@Table(name = "AUTHORITIES")
public class Authorities extends AbstractEntity {

    @Column(name = "AUTHORITY_NAME", length = 20)
    private String authorityName;
    @Column(name = "DESCRIPTION", length = 256)
    private String description;

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
