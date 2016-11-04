package com.springroll.orm.mdm;

import java.util.List;

/**
 * Created by anishjoseph on 04/11/16.
 */
public class MdmDefinition {
    private String master;
    private String dataQuery;
    private List<ColDef> colDefs;

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getDataQuery() {
        return dataQuery;
    }

    public void setDataQuery(String dataQuery) {
        this.dataQuery = dataQuery;
    }

    public List<ColDef> getColDefs() {
        return colDefs;
    }

    public void setColDefs(List<ColDef> colDefs) {
        this.colDefs = colDefs;
    }
}
