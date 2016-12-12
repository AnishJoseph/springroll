package com.springroll.reporting.grid;

import com.springroll.core.services.reporting.IGridColumn;
import com.springroll.core.services.reporting.IGridReport;

import java.util.List;

/**
 * Created by anishjoseph on 21/10/16.
 */
public class GridReport implements IGridReport {
    private List<? extends IGridColumn> columns;
    private List<Object> data;

    @Override
    public List<? extends IGridColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<? extends IGridColumn> columns) {
        this.columns = columns;
    }

    @Override
    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }
}
