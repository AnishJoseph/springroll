package com.springroll.mdm;

import com.springroll.core.DTO;
import com.springroll.core.DTOBusinessValidator;
import com.springroll.core.IBusinessValidationResults;
import com.springroll.core.SpringrollSecurity;
import com.springroll.core.exceptions.SpringrollException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
                boolean isNumberCol = isNumberCol(colDef.getType());
                Double value = null;
                if(isNumberCol){
                    value = Double.parseDouble((String)mdmChangedColumn.getVal());
                }
                if(!colDef.isNullable() && mdmChangedColumn.getVal() == null){
                    businessValidationResults.addBusinessViolation(1, fldName, "mdm.validation.notNullable", new String[]{fldName});
                    hasValidationErrors = true;
                }
                if(colDef.getType().equalsIgnoreCase("text") && colDef.getSizeMin() != null && mdmChangedColumn.getVal().toString().length() < colDef.getSizeMin()){
                    businessValidationResults.addBusinessViolation(1, fldName, "mdm.validation.minSize", new String[]{colDef.getSizeMin().toString()});
                    hasValidationErrors = true;
                }
                if(colDef.getType().equalsIgnoreCase("text") && colDef.getSizeMax() != null && mdmChangedColumn.getVal().toString().length() > colDef.getSizeMax()){
                    businessValidationResults.addBusinessViolation(1, fldName, "mdm.validation.maxSize", new String[]{colDef.getSizeMax().toString()});
                    hasValidationErrors = true;
                }
                if(isNumberCol && colDef.getMax() != null && value > colDef.getMax()){
                    businessValidationResults.addBusinessViolation(1, fldName, "mdm.validation.max", new String[]{getValue(colDef.getType(), colDef.getMax())});
                    hasValidationErrors = true;
                }
                if(isNumberCol && colDef.getMin() != null && value < colDef.getMin()){
                    businessValidationResults.addBusinessViolation(1, fldName, "mdm.validation.min", new String[]{getValue(colDef.getType(), colDef.getMin())});
                    hasValidationErrors = true;
                }
            }
        }

        if(!hasValidationErrors) {
            businessValidationResults.addReviewNeeded(0, "fld", "rule4", new String[]{mdmDTO.getMaster(), SpringrollSecurity.getUser().getUsername()}, "MdmMasterRule");
        }
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

    private String getValue(String type, Number value){
        if("int".equalsIgnoreCase(type)){
            Integer i = value.intValue();
            return i.toString();
        }
        Double d = value.doubleValue();
        return d.toString();
    }
}

