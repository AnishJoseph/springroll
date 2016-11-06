package com.springroll.orm.mdm;

import java.io.Serializable;
import java.util.List;

/**
 * Created by anishjoseph on 05/11/16.
 */
public class MdmChangedRecord implements Serializable{
    private long id;
    private List<MdmChangedColumn> mdmChangedColumns;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<MdmChangedColumn> getMdmChangedColumns() {
        return mdmChangedColumns;
    }

    public void setMdmChangedColumns(List<MdmChangedColumn> mdmChangedColumns) {
        this.mdmChangedColumns = mdmChangedColumns;
    }
}
