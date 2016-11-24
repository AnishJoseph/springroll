package com.springroll.mdm;

import com.springroll.core.BusinessValidationResult;
import com.springroll.core.ReviewLog;
import com.springroll.core.SpringrollUser;
import com.springroll.core.notification.INotificationMessage;
import com.springroll.core.services.IMdmReviewNotificationMessageFactory;
import com.springroll.notification.AbstractNotificationMessageFactory;
import com.springroll.review.ReviewManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by anishjoseph on 05/10/16.
 */
@Component public class MdmReviewNotificationMessageFactory extends AbstractNotificationMessageFactory implements IMdmReviewNotificationMessageFactory {
    @Autowired ReviewManager reviewManager;
    @Autowired MdmManager mdmManager;
    @PersistenceContext EntityManager em;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); //FIXME take this from properties file
    private DateTimeFormatter datetimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"); //FIXME take this from properties file

    @Override public INotificationMessage makeMessage(List<Long> reviewStepIds, String approver, List<BusinessValidationResult> businessValidationResults, SpringrollUser initiator, String serviceName, List<ReviewLog> reviewLog){
        MdmDTO mdmDTO = (MdmDTO) reviewManager.getFirstPayload(reviewStepIds.get(0));
        List<ColDef> colDefs = mdmManager.getColDefs(mdmDTO.getMaster());

        if(!mdmDTO.getChangedRecords().isEmpty()) {
            List<Long> changedIds = mdmDTO.getChangedRecords().stream().map(MdmChangedRecord::getId).collect(Collectors.toList());
            changedIds.sort((p1, p2) -> p1.compareTo(p2));
            List<Object[]> existingValues = mdmManager.getData(mdmDTO.getMaster(), changedIds);

            List<String> colNames = colDefs.stream().map(ColDef::getName).collect(Collectors.toList());

            for (int i = 0; i < changedIds.size(); i++) {
                Object[] existingValuesForThisRecord = existingValues.get(i);
                for (MdmChangedRecord changedRecord : mdmDTO.getChangedRecords()) {
                    if (changedRecord.getId() == changedIds.get(i)) {
                        Map<String, MdmChangedColumn> unchangedCols = new HashMap<>();
                        Set<String> changedCols = changedRecord.getMdmChangedColumns().keySet();
                        for (int j = 0; j < colNames.size(); j++) {
                            if (changedCols.contains(colNames.get(j))) continue;
                            unchangedCols.put(colNames.get(j), new MdmChangedColumn(existingValuesForThisRecord[j], existingValuesForThisRecord[j]));
                            if(existingValuesForThisRecord[j] != null && existingValuesForThisRecord[j].getClass().equals(LocalDate.class)){
                                String formattedDate = ((LocalDate) existingValuesForThisRecord[j]).format(dateFormatter);
                                unchangedCols.put(colNames.get(j), new MdmChangedColumn(formattedDate, formattedDate));
                            } else if (existingValuesForThisRecord[j] != null && existingValuesForThisRecord[j].getClass().equals(LocalDateTime.class)){
                                String formattedDate = ((LocalDateTime) existingValuesForThisRecord[j]).format(datetimeFormatter);
                                unchangedCols.put(colNames.get(j), new MdmChangedColumn(formattedDate, formattedDate));
                            }

                            //FIXME - handle date and other types
                        }
                        changedRecord.getMdmChangedColumns().putAll(unchangedCols);

                        break; //Go to the next changedId
                    }
                }
            }
        }

        MdmChangesForReview mdmChangesForReview = new MdmChangesForReview(mdmDTO, colDefs);

        return new MdmReviewNotificationMessage(reviewStepIds, approver, "ui.mdm.review.noti.msg", businessValidationResults.get(0).getArgs(), mdmChangesForReview);
    }
}
