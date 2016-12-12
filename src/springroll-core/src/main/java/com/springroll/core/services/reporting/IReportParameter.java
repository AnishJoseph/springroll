package com.springroll.core.services.reporting;

import com.springroll.core.Lov;
import com.springroll.core.SetTime;

import java.util.List;

/**
 * Created by anishjoseph on 12/12/16.
 */
public interface IReportParameter {
    String getName();
    String getJavaType();
    boolean isConstrained();
    List<Lov> getLovList();
    boolean isMandatory();
    boolean isMultiSelect();
    boolean isVisible();
    SetTime getSetTime();
}
