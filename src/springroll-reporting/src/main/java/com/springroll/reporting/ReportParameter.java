package com.springroll.reporting;

import com.springroll.core.Lov;
import com.springroll.core.SetTime;
import com.springroll.core.services.reporting.IReportParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anishjoseph on 18/10/16.
 */
public class ReportParameter implements IReportParameter {
    private String name;
    private String javaType;
    private boolean isConstrained = false;
    private List<Lov> lovList = new ArrayList<>();
    private boolean mandatory = false;
    private boolean multiSelect = false;
    private boolean visible = true;
    private SetTime setTime;
    private int width;


    public ReportParameter(){}
    public ReportParameter(String name, String javaType, boolean isConstrained, boolean mandatory, boolean multiSelect, boolean visible, List<Lov> lovs, SetTime setTime, int width) {
        this.name = name;
        this.javaType = javaType;
        this.isConstrained = isConstrained;
        this.mandatory = mandatory;
        this.multiSelect = multiSelect;
        this.visible = visible;
        this.lovList = lovs;
        this.setTime = setTime;
        this.width = width;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    @Override
    public boolean isConstrained() {
        return isConstrained;
    }

    public void setConstrained(boolean constrained) {
        isConstrained = constrained;
    }

    @Override
    public List<Lov> getLovList() {
        return lovList;
    }

    public void setLovList(List<Lov> lovList) {
        this.lovList = lovList;
    }

    @Override
    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    @Override
    public boolean isMultiSelect() {
        return multiSelect;
    }

    public void setMultiSelect(boolean multiSelect) {
        this.multiSelect = multiSelect;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public SetTime getSetTime() {
        return setTime;
    }

    public void setSetTime(SetTime setTime) {
        this.setTime = setTime;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
