package com.springroll.mdm;

import java.util.List;

/**
 * Created by anishjoseph on 03/11/16.
 */
public class MdmData {
    private List<Object[]> data;
    private List<ColDef> colDefs;

    public List<Object[]> getData() {
        return data;
    }

    public void setData(List<Object[]> data) {
        this.data = data;
    }

    public List<ColDef> getColDefs() {
        return colDefs;
    }

    public void setColDefs(List<ColDef> colDefs) {
        this.colDefs = colDefs;
    }
}
