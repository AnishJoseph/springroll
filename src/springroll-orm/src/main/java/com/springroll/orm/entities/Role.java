package com.springroll.orm.entities;

import com.springroll.orm.CSVListConverter;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anishjoseph on 05/09/16.
 */
@NamedQueries({
        @NamedQuery(name="Role.getAllRoles", query = "select r.roleName from Role r"),
        @NamedQuery(name="Role.getAuthorizationsForRoles", query = "select r.authorizations from Role r WHERE r.roleName in ?1")
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

    @NotNull
    @Size(min = 1, message = "role.validation.empty")
    @Column(name = "AUTHORIZATIONS")
    @Convert(converter = CSVListConverter.class)
    private List<String> authorizations = new ArrayList<>();

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

    public List<String> getAuthorizations() {
        return authorizations;
    }

    public void setAuthorizations(List<String> authorizations) {
        this.authorizations = authorizations;
    }
}
