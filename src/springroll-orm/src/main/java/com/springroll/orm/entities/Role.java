package com.springroll.orm.entities;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by anishjoseph on 05/09/16.
 */
@NamedQueries({
        @NamedQuery(name="Role.getAllRoles", query = "select r.roleName from Role r")
})
@Entity
@Table(name = "ROLE")
@Audited
public class Role extends MdmEntity {

    @Column(name = "ROLE_NAME", length = 32)
    @NotNull
    @Size(max = 32, min = 1)
    private String roleName;

    @Column(name = "ACTIVE", length = 1)
    @NotNull
    private boolean active;

    @Column(name = "DESCRIPTION", length = 256)
    @Size(max = 256, min = 1)
    @NotNull
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
