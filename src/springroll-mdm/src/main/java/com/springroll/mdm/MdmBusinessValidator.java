package com.springroll.mdm;

import com.springroll.core.DTO;
import com.springroll.core.DTOBusinessValidator;
import com.springroll.core.IBusinessValidationResults;
import com.springroll.core.SpringrollSecurity;
import com.springroll.core.exceptions.SpringrollException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by anishjoseph on 09/11/16.
 */
@Service
public class MdmBusinessValidator implements DTOBusinessValidator {
    @Autowired private MdmManager mdmManager;
    @Override
    public void validate(List<? extends DTO> dtos, IBusinessValidationResults businessValidationResults) {
        MdmDTO mdmDTO = (MdmDTO)dtos.get(0);
        List<ColDef> colDefs = mdmManager.getColDefs(mdmDTO.getMaster());
        boolean hasValidationErrors = false;
        for (MdmChangedRecord mdmChangedRecord : mdmDTO.getChangedRecords()) {
            for (String fldName : mdmChangedRecord.getMdmChangedColumns().keySet()) {
                ColDef colDef = getColDef(fldName, colDefs);
                if(colDef == null)throw new SpringrollException("mdm.unknownColumn", fldName, mdmDTO.getMaster());
                MdmChangedColumn mdmChangedColumn = mdmChangedRecord.getMdmChangedColumns().get(fldName);
                hasValidationErrors = validate(fldName, mdmChangedColumn.getVal(), colDef, businessValidationResults);
            }
        }
        for (Map<String, Object> newRecord : mdmDTO.getNewRecords()) {
            for (String fldName : newRecord.keySet()) {
                if("id".equalsIgnoreCase(fldName))continue;
                ColDef colDef = getColDef(fldName, colDefs);
                if(colDef == null)throw new SpringrollException("mdm.unknownColumn", fldName, mdmDTO.getMaster());
                hasValidationErrors = validate(fldName, newRecord.get(fldName), colDef, businessValidationResults);
            }
        }

        if(!hasValidationErrors) {
            businessValidationResults.addReviewNeeded(0, "fld", "rule4", new String[]{mdmDTO.getMaster(), SpringrollSecurity.getUser().getUsername()}, "MdmMasterRule");
        }
    }

    private boolean validate(String fldName, Object valueToValidate, ColDef colDef, IBusinessValidationResults businessValidationResults){
        boolean isNumberCol = isNumberCol(colDef.getType());
        boolean hasValidationErrors = false;
        Number value = null;
        String messagekey = colDef.getMessageKey();
        if(isNumberCol){
            value = valueToValidate != null ? getValue(colDef.getType(), valueToValidate) : null;
        }
        if(!colDef.isNullable() && valueToValidate == null){
            if(messagekey == null)messagekey = "mdm.validation.notNullable";
            businessValidationResults.addBusinessViolation(1, fldName, messagekey, new String[]{fldName});
            hasValidationErrors = true;
        }
        if(valueToValidate != null && colDef.getType().equalsIgnoreCase("text") && colDef.getSizeMin() != null && valueToValidate.toString().length() < colDef.getSizeMin()){
            if(messagekey == null)messagekey = "mdm.validation.minSize";
            businessValidationResults.addBusinessViolation(1, fldName, messagekey, new String[]{fldName, colDef.getSizeMin().toString()});
            hasValidationErrors = true;
        }
        if(valueToValidate != null && colDef.getType().equalsIgnoreCase("text") && colDef.getSizeMax() != null && valueToValidate.toString().length() > colDef.getSizeMax()){
            if(messagekey == null)messagekey = "mdm.validation.maxSize";
            businessValidationResults.addBusinessViolation(1, fldName, messagekey, new String[]{fldName, colDef.getSizeMax().toString()});
            hasValidationErrors = true;
        }
        if(valueToValidate != null && isNumberCol && colDef.getMax() != null && value.doubleValue() > colDef.getMax()){
            if(messagekey == null)messagekey = "mdm.validation.max";
            businessValidationResults.addBusinessViolation(1, fldName, messagekey, new String[]{fldName, getValueString(colDef.getType(), colDef.getMax())});
            hasValidationErrors = true;
        }
        if(valueToValidate != null && isNumberCol && colDef.getMin() != null && value.doubleValue() < colDef.getMin()){
            if(messagekey == null)messagekey = "mdm.validation.min";
            businessValidationResults.addBusinessViolation(1, fldName, messagekey, new String[]{fldName, getValueString(colDef.getType(), colDef.getMin())});
            hasValidationErrors = true;
        }
        if(valueToValidate != null && !isNumberCol && colDef.getPattern() != null && !colDef.getPattern().matcher(valueToValidate+"").matches()){
            if(messagekey == null)messagekey = "mdm.validation.pattern";
            businessValidationResults.addBusinessViolation(1, fldName, messagekey, new String[]{fldName, colDef.getPattern().toString()});
            hasValidationErrors = true;
        }
        return hasValidationErrors;

    }
    private ColDef getColDef(String colName, List<ColDef> colDefs){
        for (ColDef colDef : colDefs) {
            if(colDef.getName().equalsIgnoreCase(colName))return colDef;
        }
        return null;
    }

    private boolean isNumberCol(String type){
        return (type.equalsIgnoreCase("int") || type.equalsIgnoreCase("num"));
    }

    private Number getValue(String type, Object value){
        String v = value + "";
        if("int".equalsIgnoreCase(type)){
            Integer i = Integer.parseInt(v);
            return i;
        }
        Double d = Double.parseDouble(v);
        return d;

    }
    private String getValueString(String type, Number value){
        if("int".equalsIgnoreCase(type)){
            return value.intValue() + "";
        }
        return value.doubleValue() + "";
    }
}

