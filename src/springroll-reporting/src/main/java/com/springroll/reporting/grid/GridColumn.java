package com.springroll.reporting.grid;

import com.springroll.core.Align;
import com.springroll.core.services.reporting.IGridColumn;

/**
 * Created by anishjoseph on 18/10/16.
 * Defines a
 */
public class GridColumn implements IGridColumn {
    private String title;
    private String type = "text";
    private String numberFormat;
    private boolean visible = true;
    private Align align = Align.LEFT;
    private String format;
    private String width;
    private boolean sortable = true;

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
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public Align getAlign() {
        return align;
    }

    public void setAlign(Align align) {
        this.align = align;
    }

    @Override
    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    @Override
    public boolean isSortable() {
        return sortable;
    }

    public void setSortable(boolean sortable) {
        this.sortable = sortable;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        type = "num-fmt";
        this.format = format;
    }
}
