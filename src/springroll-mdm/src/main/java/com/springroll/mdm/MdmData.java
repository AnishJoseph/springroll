package com.springroll.mdm;

import com.springroll.core.services.mdm.IMdmData;
import com.springroll.core.services.mdm.MdmColumnDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anishjoseph on 03/11/16.
 */
public class MdmData implements IMdmData {
    private List<Object[]> data;
    private List<MdmColumnDefinition> colDefs;
    private List<Long> recIdsUnderReview = new ArrayList<>();

    @Override
    public List<Object[]> getData() {
        return data;
    }

    public void setData(List<Object[]> data) {
        this.data = data;
    }

    @Override
    public List<MdmColumnDefinition> getColDefs() {
        return colDefs;
    }

    public void setColDefs(List<MdmColumnDefinition> colDefs) {
        this.colDefs = colDefs;
    }

    @Override
    public List<Long> getRecIdsUnderReview() {
        return recIdsUnderReview;
    }

    public void setRecIdsUnderReview(List<Long> recIdsUnderReview) {
        this.recIdsUnderReview = recIdsUnderReview;
    }
}
