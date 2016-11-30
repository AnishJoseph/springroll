package com.springroll.mdm;

import com.springroll.core.DTO;
import com.springroll.core.DTOBusinessValidator;
import com.springroll.core.IBusinessValidationResults;
import com.springroll.core.SpringrollSecurity;
import com.springroll.core.exceptions.SpringrollException;
import com.springroll.orm.entities.ReviewStepMeta;
import com.springroll.orm.repositories.Repositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by anishjoseph on 09/11/16.
 */
@Service
public class MdmBusinessValidator implements DTOBusinessValidator {
    private static final String CID = "cid";
    @PersistenceContext
    EntityManager em;
    @Autowired
    Repositories repositories;

    @Autowired private MdmManager mdmManager;
    @Override
    public void validate(List<? extends DTO> dtos, IBusinessValidationResults businessValidationResults) {
        MdmDTO mdmDTO = (MdmDTO)dtos.get(0);
        MdmDefinition mdmDefinition = mdmManager.getDefinition(mdmDTO.getMaster());
        List<ColDef> colDefs = mdmDefinition.getColDefs();
        boolean hasValidationErrors = false;
        for (MdmChangedRecord mdmChangedRecord : mdmDTO.getChangedRecords()) {
            String cid = mdmChangedRecord.getMdmChangedColumns().remove(CID).getVal().toString();
            for (String fldName : mdmChangedRecord.getMdmChangedColumns().keySet()) {
                ColDef colDef = getColDef(fldName, colDefs);
                if(colDef == null)throw new SpringrollException("mdm.unknownColumn", fldName, mdmDTO.getMaster());
                MdmChangedColumn mdmChangedColumn = mdmChangedRecord.getMdmChangedColumns().get(fldName);
                hasValidationErrors = validate(fldName, mdmChangedColumn.getVal(), colDef, businessValidationResults, cid);
            }
//            if(!hasValidationErrors) {
//                if (!validateConstraintsForExistingRecords(mdmDefinition, mdmChangedRecord)) {
//                    mdmDefinition.getConstraints().forEach(s -> businessValidationResults.addBusinessViolation(cid, 1, s, "non-uniq", new String[]{}));
//                    hasValidationErrors = true;
//                }
//            }
        }
        for (Map<String, Object> newRecord : mdmDTO.getNewRecords()) {
            String cid = newRecord.remove(CID).toString();
            for (String fldName : newRecord.keySet()) {
                if("id".equalsIgnoreCase(fldName))continue;
                ColDef colDef = getColDef(fldName, colDefs);
                if(colDef == null)throw new SpringrollException("mdm.unknownColumn", fldName, mdmDTO.getMaster());
                hasValidationErrors = validate(fldName, newRecord.get(fldName), colDef, businessValidationResults, cid);
            }
            if(!hasValidationErrors) {
                if (!validateConstraintsForNewRecords(mdmDefinition, newRecord)) {
                    mdmDefinition.getConstraints().forEach(s -> businessValidationResults.addBusinessViolation(cid, 1, s, "non-uniq", new String[]{}));
                    hasValidationErrors = true;
                }
            }
        }

        if(!hasValidationErrors) {
            businessValidationResults.addReviewNeeded(null, 0, "fld", "rule4", new String[]{mdmDTO.getMaster(), SpringrollSecurity.getUser().getUsername()}, "MdmMasterRule");
        }
    }

    private boolean validate(String fldName, Object valueToValidate, ColDef colDef, IBusinessValidationResults businessValidationResults, String  id){
        boolean isNumberCol = isNumberCol(colDef.getType());
        Number valueToValidateAsNumber = null;
        String messageKey = colDef.getMessageKey();
        if(isNumberCol){
            valueToValidateAsNumber = !isNullOrEmpty(valueToValidate) ? getValue(colDef.getType(), valueToValidate) : null;
        }

        /*  If the colDef specifies that the value CANNOT be NULL and the changed value is either null OR EMPTY, THEN we have a violation */
        if(!colDef.isNullable() && isNullOrEmpty(valueToValidate)){
            if(messageKey == null)messageKey = "mdm.validation.notNullable";
            businessValidationResults.addBusinessViolation(id, 1, fldName, messageKey, new String[]{});
            return true;
        }
        if(valueToValidate != null && colDef.getType().equalsIgnoreCase("text") && colDef.getSizeMin() != null && valueToValidate.toString().length() < colDef.getSizeMin()){
            if(messageKey == null)messageKey = "mdm.validation.minSize";
            businessValidationResults.addBusinessViolation(id, 1, fldName, messageKey, new String[]{colDef.getSizeMin().toString()});
            return true;
        }
        if(valueToValidate != null && colDef.getType().equalsIgnoreCase("text") && colDef.getSizeMax() != null && valueToValidate.toString().length() > colDef.getSizeMax()){
            if(messageKey == null)messageKey = "mdm.validation.maxSize";
            businessValidationResults.addBusinessViolation(id, 1, fldName, messageKey, new String[]{colDef.getSizeMax().toString()});
            return true;
        }
        /*  If this is a col that holds a number, AND the value received from the UI is not null, AND the definition of the column specifies a constraint with a MAX value,
            AND the value of the user entered value is greater than the MAX value specified THEN we have a violation
         */
        if(isNumberCol && valueToValidateAsNumber != null && colDef.getMax() != null && valueToValidateAsNumber.doubleValue() > colDef.getMax()){
            if(messageKey == null)messageKey = "mdm.validation.max";
            businessValidationResults.addBusinessViolation(id, 1, fldName, messageKey, new String[]{getValueString(colDef.getType(), colDef.getMax())});
            return true;
        }
        /*  If this is a col that holds a number, AND the value received from the UI is not null, AND the definition of the column specifies a constraint with a MIN value,
            AND the value of the user entered value is less than the MIN value specified THEN we have a violation
         */
        if(isNumberCol && valueToValidateAsNumber != null && colDef.getMin() != null && valueToValidateAsNumber.doubleValue() < colDef.getMin()){
            if(messageKey == null)messageKey = "mdm.validation.min";
            businessValidationResults.addBusinessViolation(id, 1, fldName, messageKey, new String[]{getValueString(colDef.getType(), colDef.getMin())});
            return true;
        }
        /*  If the changed value is NOT null AND this is a col that holds TEXT,  AND the definition of the column specifies a regex based constraint,
            AND the changed value does not match the PATTERN, THEN we have a violation
         */
        if(valueToValidate != null && "text".equalsIgnoreCase(colDef.getType()) && colDef.getPattern() != null && !colDef.getPattern().matcher(valueToValidate+"").matches()){
            if(messageKey == null)messageKey = "mdm.validation.pattern";
            businessValidationResults.addBusinessViolation(id, 1, fldName, messageKey, new String[]{colDef.getPattern().toString()});
            return true;
        }
        return false;

    }
    private boolean validateConstraintsForExistingRecords(MdmDefinition mdmDefinition, MdmChangedRecord  mdmChangedRecord){
        if(mdmDefinition.getQueryForConstraintValidation() == null)return true;
        Query query = em.createNamedQuery(mdmDefinition.getQueryForConstraintValidation());
        mdmDefinition.getConstraints().forEach(s -> query.setParameter(s, mdmChangedRecord.getMdmChangedColumns().get(s).getVal()));
        List resultList = query.getResultList();
        return resultList.isEmpty();

    }
    private boolean validateConstraintsForNewRecords(MdmDefinition mdmDefinition, Map<String, Object>  newRecord){
        if(mdmDefinition.getQueryForConstraintValidation() == null)return true;
        Query query = em.createNamedQuery(mdmDefinition.getQueryForConstraintValidation());
        mdmDefinition.getConstraints().forEach(s -> query.setParameter(s, newRecord.get(s)));
        List resultList = query.getResultList();
        if(!resultList.isEmpty()) return false;
        List<ReviewStepMeta> reviewStepMetas = repositories.reviewStepMeta.findBySearchId(MdmManager.SEARCH_ID_PREFIX + mdmDefinition.getMasterClass().getSimpleName());
        for (ReviewStepMeta reviewStepMeta : reviewStepMetas) {
            MdmDTO mdmDTO = (MdmDTO) reviewStepMeta.getEvent().getPayload();
            if(mdmDTO.getNewRecords() == null || mdmDTO.getNewRecords().isEmpty())continue;
            for (Map<String, Object> recordUnderReview : mdmDTO.getNewRecords()) {
                for (String constraintColName : mdmDefinition.getConstraints()) {
                    if(recordUnderReview.get(constraintColName).equals(newRecord.get(constraintColName)))return false;
                }
            }
        }
        return true;
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

    private boolean isNullOrEmpty(Object value){
        return value == null || value.toString().isEmpty();
    }
}

