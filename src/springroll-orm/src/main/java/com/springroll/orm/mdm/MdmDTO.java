package com.springroll.orm.mdm;

import com.springroll.core.IDTOProcessors;
import com.springroll.core.ServiceDTO;

import java.util.Map;

/**
 * Created by anishjoseph on 05/11/16.
 */
public class MdmDTO implements ServiceDTO {
    private Map<String, Map<String, MdmAttrChange>> changes;
    private Map<String, Map<String, Object>> newrecords;

    public Map<String, Map<String, MdmAttrChange>> getChanges() {
        return changes;
    }

    public void setChanges(Map<String, Map<String, MdmAttrChange>> changes) {
        this.changes = changes;
    }

    public Map<String, Map<String, Object>> getNewrecords() {
        return newrecords;
    }

    public void setNewrecords(Map<String, Map<String, Object>> newrecords) {
        this.newrecords = newrecords;
    }

    @Override
    public IDTOProcessors getProcessor() {
        return null;
    }
}
