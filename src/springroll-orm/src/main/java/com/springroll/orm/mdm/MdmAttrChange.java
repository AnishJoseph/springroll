package com.springroll.orm.mdm;

/**
 * Created by anishjoseph on 05/11/16.
 */
public class MdmAttrChange {
    private Object val;
    private Object prevVal;

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
