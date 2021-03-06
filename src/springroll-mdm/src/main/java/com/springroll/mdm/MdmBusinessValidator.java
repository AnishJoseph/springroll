package com.springroll.mdm;

import com.springroll.core.*;
import com.springroll.orm.entities.MdmEntity;
import com.springroll.orm.entities.ReviewStepMeta;
import com.springroll.orm.repositories.Repositories;
import org.springframework.beans.PropertyAccessException;
import org.springframework.beans.PropertyBatchUpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by anishjoseph on 09/11/16.
 */
@Service
public class MdmBusinessValidator implements DTOBusinessValidator {
    private static final String CID = "cid";
    @PersistenceContext EntityManager em;

    @Autowired Repositories repositories;

    @Autowired private Validator validator;

    @Autowired private MdmManager mdmManager;

    @Autowired private DefaultConversionService conversionService;

    @Autowired SpringrollUtils springrollUtils;

    @Override
    public void validate(List<? extends DTO> dtos, IBusinessValidationResults businessValidationResults) {
        MdmDTO mdmDTO = (MdmDTO)dtos.get(0);
        MdmDefinition mdmDefinition = mdmManager.getDefinition(mdmDTO.getMaster());
        boolean hasNoErrorsInChangedRecords = true, hasNoErrorsInNewRecords = true;
        for (MdmChangedRecord mdmChangedRecord : mdmDTO.getChangedRecords()) {
            String cid = mdmChangedRecord.getMdmChangedColumns().get(CID).getVal().toString();

            try {
                MdmEntity mdmEntity = mdmManager.createEntityFromChangedRecords(mdmDefinition, mdmChangedRecord, true);
                hasNoErrorsInChangedRecords = validate(mdmEntity, businessValidationResults, cid);
            } catch (PropertyBatchUpdateException e) {
                hasNoErrorsInChangedRecords = false;
                for (PropertyAccessException propertyAccessException : e.getPropertyAccessExceptions()) {
                    businessValidationResults.addBusinessViolation(cid, 1, propertyAccessException.getPropertyName(), propertyAccessException.getErrorCode(), null);
                }
            }
        }

        for (Map<String, Object> newRecord : mdmDTO.getNewRecords()) {
            String cid = newRecord.remove(CID).toString();
            try {
                MdmEntity mdmEntity = mdmManager.createEntityFromNewRecord(mdmDefinition, newRecord);
                hasNoErrorsInNewRecords = validate(mdmEntity, businessValidationResults, cid);
            } catch (PropertyBatchUpdateException e) {
                hasNoErrorsInNewRecords = false;
                for (PropertyAccessException propertyAccessException : e.getPropertyAccessExceptions()) {
                    businessValidationResults.addBusinessViolation(cid, 1, propertyAccessException.getPropertyName(), propertyAccessException.getErrorCode(), null);
                }
            }
            if(hasNoErrorsInNewRecords) {
                if (!validateConstraintsForNewRecords(mdmDefinition, newRecord)) {
                    mdmDefinition.getConstraints().forEach(s -> businessValidationResults.addBusinessViolation(cid, 1, s, "non-uniq", new String[]{}));
                    hasNoErrorsInNewRecords = false;
                }
            }
        }

        if(hasNoErrorsInChangedRecords && hasNoErrorsInNewRecords) {
            businessValidationResults.addReviewNeeded(null, 0, "fld", "rule4", new String[]{mdmDTO.getMaster(), SpringrollSecurity.getUser().getUsername()}, "MdmMasterRule");
        }
    }
    private boolean validate(MdmEntity entity, IBusinessValidationResults businessValidationResults, String cid){
        Set<ConstraintViolation<MdmEntity>> violations = validator.validate(entity);
        for (ConstraintViolation<MdmEntity> constraintViolation : violations) {
            String field = constraintViolation.getPropertyPath().toString();
            businessValidationResults.addBusinessViolation(cid, 1, field, constraintViolation.getMessage(), new String[]{});
        }
        return violations.isEmpty();
    }
    private boolean validateConstraintsForNewRecords(MdmDefinition mdmDefinition, Map<String, Object>  newRecord){
        if(mdmDefinition.getQueryForConstraintValidation() == null)return true;
        Query query = em.createNamedQuery(mdmDefinition.getQueryForConstraintValidation());
        mdmDefinition.getConstraints().forEach(s -> query.setParameter(s, springrollUtils.convert(newRecord.get(s), query.getParameter(s).getParameterType())));
        List resultList = query.getResultList();
        if(!resultList.isEmpty()) return false;
        List<ReviewStepMeta> reviewStepMetas = repositories.reviewStepMeta.findBySearchId(MdmManager.SEARCH_ID_PREFIX + mdmDefinition.getMasterClass().getSimpleName());
        for (ReviewStepMeta reviewStepMeta : reviewStepMetas) {
            MdmDTO mdmDTO = (MdmDTO) reviewStepMeta.getEvent().getPayload();
            if(mdmDTO.getNewRecords() == null || mdmDTO.getNewRecords().isEmpty())continue;
            for (Map<String, Object> recordUnderReview : mdmDTO.getNewRecords()) {
                boolean gotMatch = true;
                for (String constraintColName : mdmDefinition.getConstraints()) {
                    if(!recordUnderReview.get(constraintColName).equals(newRecord.get(constraintColName))){
                        gotMatch = false;
                        break;
                    }
                }
                if(gotMatch) return false;
            }

        }
        return true;
    }
}

