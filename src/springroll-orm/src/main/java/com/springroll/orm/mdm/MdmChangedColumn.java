package com.springroll.orm.mdm;

/**
 * Created by anishjoseph on 05/11/16.
 */
public class MdmChangedColumn {
    private String colName;
    private Object val;
    private Object prevVal;

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public Object getVal() {
        return val;
    }

    public void setVal(Object val) {
        this.val = val;
    }

    public Object getPrevVal() {
        return prevVal;
    }

    public void setPrevVal(Object prevVal) {
        this.prevVal = prevVal;
    }
}
