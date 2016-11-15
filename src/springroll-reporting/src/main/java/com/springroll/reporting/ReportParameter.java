package com.springroll.reporting;

import com.springroll.core.Lov;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anishjoseph on 18/10/16.
 */
public class ReportParameter {
    private String name;
    private String javaType;
    private boolean isConstrained = false;
    private List<Lov> lovList = new ArrayList<>();
    private boolean mandatory = false;
    private boolean multiSelect = false;
    private boolean visible = true;

    public ReportParameter(){}
    public ReportParameter(String name, String javaType, boolean isConstrained, boolean mandatory, boolean multiSelect, boolean visible, List<Lov> lovs) {
        this.name = name;
        this.javaType = javaType;
        this.isConstrained = isConstrained;
        this.mandatory = mandatory;
        this.multiSelect = multiSelect;
        this.visible = visible;
        this.lovList = lovs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public boolean isConstrained() {
        return isConstrained;
    }

    public void setConstrained(boolean constrained) {
        isConstrained = constrained;
    }

    public List<Lov> getLovList() {
        return lovList;
    }

    public void setLovList(List<Lov> lovList) {
        this.lovList = lovList;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public boolean isMultiSelect() {
        return multiSelect;
    }

    public void setMultiSelect(boolean multiSelect) {
        this.multiSelect = multiSelect;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
