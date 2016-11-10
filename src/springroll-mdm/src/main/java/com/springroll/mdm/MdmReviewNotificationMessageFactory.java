package com.springroll.mdm;

import com.springroll.core.BusinessValidationResult;
import com.springroll.core.SpringrollUser;
import com.springroll.core.notification.INotificationMessage;
import com.springroll.core.services.IMdmReviewNotificationMessageFactory;
import com.springroll.notification.AbstractNotificationMessageFactory;
import com.springroll.review.ReviewManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by anishjoseph on 05/10/16.
 */
@Component public class MdmReviewNotificationMessageFactory extends AbstractNotificationMessageFactory implements IMdmReviewNotificationMessageFactory {
    @Autowired ReviewManager reviewManager;
    @Autowired MdmManager mdmManager;

    @Override public INotificationMessage makeMessage(List<Long> reviewStepIds, String approver, List<BusinessValidationResult> businessValidationResults, SpringrollUser initiator, String serviceName){
        MdmDTO mdmDTO = (MdmDTO) reviewManager.getFirstPayload(reviewStepIds.get(0));
        List<ColDef> colDefs = mdmManager.getColDefs(mdmDTO.getMaster());
        List<MdmChangedRecord> changedRecords = mdmDTO.getChangedRecords();
        List<String> colNames = colDefs.stream().map(ColDef::getName).collect(Collectors.toList());
        for (MdmChangedRecord changedRecord : changedRecords) {
            Map<String, MdmChangedColumn> unchangedCols = new HashMap<>();
            Set<String> changedCols = changedRecord.getMdmChangedColumns().keySet();
            for (String colName : colNames) {
                if(changedCols.contains(colName))continue;
                unchangedCols.put(colName, new MdmChangedColumn("NC", "NC"));
            }
            changedRecord.getMdmChangedColumns().putAll(unchangedCols);
        }

        MdmChangesForReview mdmChangesForReview = new MdmChangesForReview(mdmDTO, colDefs);

        return new MdmReviewNotificationMessage(reviewStepIds, approver, "ui.mdm.review.noti.msg", businessValidationResults.get(0).getArgs(), mdmChangesForReview);
    }
}
