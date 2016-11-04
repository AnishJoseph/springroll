package com.springroll.orm.mdm;

import com.springroll.core.Lov;

import java.util.List;

/**
 * Created by anishjoseph on 03/11/16.
 */

public class ColDef {
    private String name;
    private boolean writeable;
    private String type;
    private List<Lov> lovList;
    private Object defaultValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isWriteable() {
        return writeable;
    }

    public void setWriteable(boolean writeable) {
        this.writeable = writeable;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Lov> getLovList() {
        return lovList;
    }

    public void setLovList(List<Lov> lovList) {
        this.lovList = lovList;
    }
}
