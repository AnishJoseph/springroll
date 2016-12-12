package com.springroll.core.services.reporting;


import java.util.List;

/**
 * Created by anishjoseph on 12/12/16.
 */
public interface IGridReport {
    List<? extends IGridColumn> getColumns();
    List<Object> getData();
}
