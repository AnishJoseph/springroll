package com.springroll.mdm;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by anishjoseph on 05/11/16.
 */
public class MdmChangedRecord implements Serializable{
    private long id;
    private Map<String, MdmChangedColumn> mdmChangedColumns; //The Key is the name of the column

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Map<String, MdmChangedColumn> getMdmChangedColumns() {
        return mdmChangedColumns;
    }

    public void setMdmChangedColumns(Map<String, MdmChangedColumn> mdmChangedColumns) {
        this.mdmChangedColumns = mdmChangedColumns;
    }
}
