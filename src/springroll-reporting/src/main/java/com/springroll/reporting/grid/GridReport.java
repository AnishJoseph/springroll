package com.springroll.reporting.grid;

import java.util.List;

/**
 * Created by anishjoseph on 21/10/16.
 */
public class GridReport {
    private List<Column> columns;
    private List<Object> data;

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }
}
