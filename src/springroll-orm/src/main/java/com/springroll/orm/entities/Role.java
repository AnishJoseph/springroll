package com.springroll.orm.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by anishjoseph on 05/09/16.
 */
@NamedQueries({
        @NamedQuery(name="Role.getRecordsForMdm", query = "SELECT o.id, o.roleName, o.description, o.active FROM Role o"),
        @NamedQuery(name="Role.getAllRoles", query = "select r.roleName from Role r")
})
@Entity
@Table(name = "ROLE")
public class Role extends AbstractEntity {

    @Column(name = "ROLE_NAME", length = 32)
    private String roleName;

    @Column(name = "ACTIVE", length = 1)
    @NotNull
    private boolean active;

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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
