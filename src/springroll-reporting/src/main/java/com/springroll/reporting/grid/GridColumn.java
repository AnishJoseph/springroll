package com.springroll.reporting.grid;

import com.springroll.core.services.reporting.IGridColumn;

/**
 * Created by anishjoseph on 18/10/16.
 * Defines a
 */
public class GridColumn implements IGridColumn {
    private String title;
    private String type = "text";
    private String numberFormat;
    private String className;

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        if(numberFormat != null) type = "num-fmt";
        this.type = type;
    }

    public String getNumberFormat() {
        return numberFormat;
    }

    public void setNumberFormat(String numberFormat) {
        type = "num-fmt";
        this.numberFormat = numberFormat;
    }

    @Override
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
