package com.springroll.mdm;

import com.springroll.core.IDTOProcessors;
import com.springroll.router.CoreDTOProcessors;
import com.springroll.router.dto.IMdmDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by anishjoseph on 05/11/16.
 */
public class MdmDTO implements IMdmDTO {
    private String master;
    private List<MdmChangedRecord> changedRecords;
    private List<Map<String, Object>> newRecords;

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

    @Override
    public IDTOProcessors getProcessor() {
        return CoreDTOProcessors.MDM;
    }
}
