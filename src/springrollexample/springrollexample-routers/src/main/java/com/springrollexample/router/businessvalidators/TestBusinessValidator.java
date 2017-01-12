package com.springrollexample.router.businessvalidators;

import com.springroll.core.DTO;
import com.springroll.core.DTOBusinessValidator;
import com.springroll.core.IBusinessValidationResults;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by anishjoseph on 27/09/16.
 */
@Service
public class TestBusinessValidator implements DTOBusinessValidator {
    @Override
    public void validate(List<? extends DTO> dtos, IBusinessValidationResults businessValidationResults) {
        businessValidationResults.addReviewNeeded(null, 0, "fld", "ui.rule1.msgkey", new String[]{"Hello1", "World1"}, "Rule1");
//        businessValidationResults.addReviewNeeded(null, 0, "fld", "ui.rule2.msgkey", new String[]{"Hello2", "World2"}, "Rule2");
//        businessValidationResults.addReviewNeeded(null, 0, "fld", "ui.rule3.msgkey", new String[]{"Hello3", "World3"}, "Rule3");
        businessValidationResults.addReviewNeeded(null, 0, "fld", "ui.rule4.msgkey", new String[]{"Hello4", "World4"}, "Rule4");
    }
}
