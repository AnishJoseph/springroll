package com.springroll.orm.entities;

/**
 * Created by anishjoseph on 08/09/16.
 */

import javax.persistence.*;

@MappedSuperclass
public abstract class AbstractEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "tableBasedIdGenerator")
    @TableGenerator(name = "tableBasedIdGenerator", table = "SEQUENCE_TABLE", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", allocationSize = 100)
    private Long id;

    @Column(name = "PARENT_ID")
    private Long parentId;

    public Long getID()
    {
        return id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}