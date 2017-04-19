package com.springroll.mdm;

import com.springroll.core.Lov;
import com.springroll.core.services.mdm.MdmColumnDefinition;

import java.io.Serializable;
import java.util.List;

/**
 * Created by anishjoseph on 03/11/16.
 */

public class ColDef implements Serializable, MdmColumnDefinition {
    private String name;
    private String title;
    private boolean writeable;
    private String type;
    private List<Lov> lovList;
    private Object defVal;
    private String lovSource;
    private boolean multiSelect = false;
    private boolean nullable = true;
    private String width = null;

    public ColDef(ColDef colDef){
        this.name = colDef.name;
        this.writeable = colDef.writeable;
        this.type = colDef.type;
        this.defVal = colDef.defVal;
        this.multiSelect = colDef.multiSelect;
        this.nullable = colDef.nullable;
        this.width = colDef.width;
        this.title = colDef.title;
    }

    public ColDef(String name, boolean writeable, String type, boolean multiSelect) {
        this.name = name;
        this.writeable = writeable;
        this.type = type;
        this.multiSelect = multiSelect;
    }

    public ColDef() {
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isWriteable() {
        return writeable;
    }

    public void setWriteable(boolean writeable) {
        this.writeable = writeable;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public List<Lov> getLovList() {
        return lovList;
    }

    public void setLovList(List<Lov> lovList) {
        this.lovList = lovList;
    }

    @Override
    public Object getDefVal() {
        return defVal;
    }

    public void setDefVal(Object defVal) {
        this.defVal = defVal;
    }

    @Override
    public String getLovSource() {
        return lovSource;
    }

    public void setLovSource(String lovSource) {
        this.lovSource = lovSource;
    }

    @Override
    public boolean isMultiSelect() {
        return multiSelect;
    }

    public void setMultiSelect(boolean multiSelect) {
        this.multiSelect = multiSelect;
    }

    @Override
    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    @Override
    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
