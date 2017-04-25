package com.springroll.core.services.reporting;

import com.springroll.core.Align;

/**
 * Created by anishjoseph on 12/12/16.
 */
public interface IGridColumn {
    String getName();
    String getTitle();
    String getType();
    boolean isVisible();
    Align getAlign();
    String getWidth();
    boolean isSortable();
}
