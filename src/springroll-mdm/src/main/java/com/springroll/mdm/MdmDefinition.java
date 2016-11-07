package com.springroll.mdm;

import com.springroll.core.exceptions.FixableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by anishjoseph on 04/11/16.
 */
public class MdmDefinition {
    private static final Logger logger = LoggerFactory.getLogger(MdmDefinition.class);
    private String master;
    private String masterClassName;
    private String dataQuery;
    private List<ColDef> colDefs;
    private Class masterClass;

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getDataQuery() {
        return dataQuery;
    }

    public void setDataQuery(String dataQuery) {
        this.dataQuery = dataQuery;
    }

    public List<ColDef> getColDefs() {
        return colDefs;
    }

    public void setColDefs(List<ColDef> colDefs) {
        this.colDefs = colDefs;
    }

    public String getMasterClassName() {
        return masterClassName;
    }

    public Class getMasterClass() {
        return masterClass;
    }

    public void setMasterClassName(String masterClassName) {
        this.masterClassName = masterClassName;
        try {
            masterClass = Class.forName(masterClassName);
        } catch (ClassNotFoundException e) {
            logger.error("Exception while getting finding MDM class with name   '{}'. Exceptions is {} ", masterClassName, e.getMessage());
            throw new FixableException("", "lov.source.missingenum", masterClassName, e.getMessage());
        }
    }

    public ColDef getColDefByName(String colName){
        for (ColDef colDef : colDefs) {
            if(colDef.getName().equals(colName))return colDef;
        }
        return null;
    }
}
