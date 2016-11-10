package com.springroll.mdm;

import com.springroll.core.SpringrollSecurity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by anishjoseph on 09/11/16.
 */

public class MdmChangesForReview implements Serializable {
    private String master;
    private String userId;
    private List<MdmChangedRecord> changedRecords;
    private List<Map<String, Object>> newRecords;
    private List<ColDef> colDefs;

    public MdmChangesForReview() {
    }
    public MdmChangesForReview(MdmDTO mdmDTO, List<ColDef> colDefs) {
        this.master = mdmDTO.getMaster();
        this.changedRecords = mdmDTO.getChangedRecords();
        this.newRecords = mdmDTO.getNewRecords();
        this.colDefs = colDefs;
        userId = SpringrollSecurity.getUser().getUsername();
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public List<MdmChangedRecord> getChangedRecords() {
        return changedRecords;
    }

    public void setChangedRecords(List<MdmChangedRecord> changedRecords) {
        this.changedRecords = changedRecords;
    }

    public List<Map<String, Object>> getNewRecords() {
        return newRecords;
    }

    public void setNewRecords(List<Map<String, Object>> newRecords) {
        this.newRecords = newRecords;
    }

    public List<ColDef> getColDefs() {
        return colDefs;
    }

    public void setColDefs(List<ColDef> colDefs) {
        this.colDefs = colDefs;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}