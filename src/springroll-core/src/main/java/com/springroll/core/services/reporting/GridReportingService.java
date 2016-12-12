package com.springroll.core.services.reporting;

import java.util.List;
import java.util.Map;

/**
 * Created by anishjoseph on 12/12/16.
 */
public interface GridReportingService {
    IGridReport getGrid(String gridName, Map<String, Object> parameters);

    List<IReportParameter> getParameters(String gridName, Map<String, Object> parameters);
}
