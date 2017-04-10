package com.springroll.orm.entities;

import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by anishjoseph on 05/09/16.
 */
@NamedQueries({
        @NamedQuery(name="Authorization.getAllAuthorizations", query = "select r.name from Authorization r")
})
@Entity
@Table(name = "AUTHORIZATION")
@Audited
public class Authorization extends MdmEntity {

    @Column(name = "NAME", length = 32)
    @NotNull
    @Size(max = 32, min = 1)
    private String name;

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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
