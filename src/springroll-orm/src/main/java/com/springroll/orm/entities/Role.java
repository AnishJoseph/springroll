package com.springroll.orm.entities;

import javax.persistence.*;

/**
 * Created by anishjoseph on 05/09/16.
 */
@NamedQueries({
        @NamedQuery(name="Role.getRecordsForMdm", query = "SELECT o.id, o.roleName, o.description FROM Role o")
})
@Entity
@Table(name = "ROLE")
public class Role extends AbstractEntity {

    @Column(name = "ROLE_NAME", length = 32)
    private String roleName;

    @Column(name = "DESCRIPTION", length = 256)
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
