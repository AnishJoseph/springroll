package com.springroll.mdm;

import com.springroll.core.SpringrollUtils;
import com.springroll.core.services.notification.IAlertMessage;
import com.springroll.core.services.notification.IReviewMeta;
import com.springroll.core.services.mdm.IMdmReviewNotificationMessageFactory;
import com.springroll.notification.AbstractReviewNotificationMessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by anishjoseph on 05/10/16.
 */
@Component public class MdmReviewAlertFactory extends AbstractReviewNotificationMessageFactory implements IMdmReviewNotificationMessageFactory {
    @Autowired MdmManager mdmManager;
    @Autowired SpringrollUtils springrollUtils;

    @Override public IAlertMessage makeMessage(IReviewMeta reviewMeta){
        MdmDTO mdmDTO = (MdmDTO) reviewMeta.getDtosUnderReview().get(0);
        List<ColDef> colDefs = mdmManager.getDefinition(mdmDTO.getMaster()).getColDefs();

        if(!mdmDTO.getChangedRecords().isEmpty()) {
            List<Long> changedIds = mdmDTO.getChangedRecords().stream().map(MdmChangedRecord::getId).collect(Collectors.toList());
            changedIds.sort((p1, p2) -> p1.compareTo(p2));
            List<Object[]> existingValues = mdmManager.getDataForSpecificRecords(mdmDTO.getMaster(), changedIds);

            List<String> colNames = colDefs.stream().map(ColDef::getName).collect(Collectors.toList());

            for (int i = 0; i < changedIds.size(); i++) {
                Object[] existingValuesForThisRecord = existingValues.get(i);
                for (MdmChangedRecord changedRecord : mdmDTO.getChangedRecords()) {
                    if (changedRecord.getId() == changedIds.get(i)) {
                        Map<String, MdmChangedColumn> unchangedCols = new HashMap<>();
                        Set<String> changedCols = changedRecord.getMdmChangedColumns().keySet();
                        for (int j = 0; j < colNames.size(); j++) {
                            if (changedCols.contains(colNames.get(j))){
                                changedRecord.getMdmChangedColumns().get(colNames.get(j)).setPrevVal(existingValuesForThisRecord[j]);
                                changedRecord.getMdmChangedColumns().get(colNames.get(j)).setChanged(true);
                                continue;
                            }
                            unchangedCols.put(colNames.get(j), new MdmChangedColumn(existingValuesForThisRecord[j], existingValuesForThisRecord[j], false));
                        }
                        changedRecord.getMdmChangedColumns().putAll(unchangedCols);

                        break; //Go to the next changedId
                    }
                }
            }
        }

        MdmChangesForReview mdmChangesForReview = new MdmChangesForReview(mdmDTO, colDefs);

        return new MdmReviewAlertMessage(reviewMeta, reviewMeta.getBusinessValidationResults().get(0).getArgs(), mdmChangesForReview);
    }
}
