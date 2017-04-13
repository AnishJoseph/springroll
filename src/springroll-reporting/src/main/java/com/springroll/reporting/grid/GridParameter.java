package com.springroll.reporting.grid;

import com.springroll.core.SetTime;

import java.util.List;

/**
 * Created by anishjoseph on 19/10/16.
 */
public class GridParameter {
    private String name;
    private boolean multiSelect;
    private boolean hidden = false;
    private String  namedQuery;
    private String  method;
    private String  bean;
    private SetTime setTime;
    private List<String> list;
    private int width = 3;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMultiSelect() {
        return multiSelect;
    }

    public void setMultiSelect(boolean multiSelect) {
        this.multiSelect = multiSelect;
    }

    public String getNamedQuery() {
        return namedQuery;
    }

    public void setNamedQuery(String namedQuery) {
        this.namedQuery = namedQuery;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public SetTime getSetTime() {
        return setTime;
    }

    public void setSetTime(SetTime setTime) {
        this.setTime = setTime;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getBean() {
        return bean;
    }

    public void setBean(String bean) {
        this.bean = bean;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
