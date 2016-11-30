package com.springroll.mdm;

import com.springroll.core.exceptions.SpringrollException;
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
    private String queryToGetMdmRecords;
    private String queryToGetSpecificMdmRecords;
    private List<ColDef> colDefs;
    private Class masterClass;
    private List<String> constraints;
    private String queryForConstraintValidation = null;

    public String getMaster() {
        return master;
    }

    public String getQueryToGetMdmRecords() {
        return queryToGetMdmRecords;
    }

    public void setQueryToGetMdmRecords(String queryToGetMdmRecords) {
        this.queryToGetMdmRecords = queryToGetMdmRecords;
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

    public String getQueryToGetSpecificMdmRecords() {
        return queryToGetSpecificMdmRecords;
    }

    public void setQueryToGetSpecificMdmRecords(String queryToGetSpecificMdmRecords) {
        this.queryToGetSpecificMdmRecords = queryToGetSpecificMdmRecords;
    }

    public void setMasterClassName(String masterClassName) {
        this.masterClassName = masterClassName;
        try {
            masterClass = Class.forName(masterClassName);
            master = masterClassName.substring(masterClassName.lastIndexOf(".")+1);
        } catch (ClassNotFoundException e) {
            logger.error("Exception while getting finding MDM class with name   '{}'. Exceptions is {} ", masterClassName, e.getMessage());
            throw new SpringrollException("lov.source.missingenum", masterClassName, e.getMessage());
        }
    }

    public ColDef getColDefByName(String colName){
        for (ColDef colDef : colDefs) {
            if(colDef.getName().equals(colName))return colDef;
        }
        return null;
    }

    public List<String> getConstraints() {
        return constraints;
    }

    public void setConstraints(List<String> constraints) {
        this.constraints = constraints;
    }

    public String getQueryForConstraintValidation() {
        return queryForConstraintValidation;
    }

    public void setQueryForConstraintValidation(String queryForConstraintValidation) {
        this.queryForConstraintValidation = queryForConstraintValidation;
    }
}
