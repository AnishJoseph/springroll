package com.springroll.mdm;

import java.io.Serializable;

/**
 * Created by anishjoseph on 05/11/16.
 */
public class MdmChangedColumn implements Serializable {
    private Object val;
    private Object prevVal;
    private boolean changed = true;

    public MdmChangedColumn() {
    }

    public MdmChangedColumn(Object val, Object prevVal, boolean changed) {
        this.val = val;
        this.prevVal = prevVal;
        this.changed = changed;
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

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }
}
