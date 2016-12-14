package com.springroll.core.services.mdm;

import java.util.List;

/**
 * Created by anishjoseph on 14/12/16.
 */
public interface IMdmData {
    List<Object[]> getData();

    List<? extends MdmColumnDefinition> getColDefs();

    List<Long> getRecIdsUnderReview();
}
