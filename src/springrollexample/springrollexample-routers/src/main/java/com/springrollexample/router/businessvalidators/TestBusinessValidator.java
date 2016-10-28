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
        businessValidationResults.addReviewNeeded(0, "fld", "rule4", null, "Rule4");
        businessValidationResults.addReviewNeeded(0, "fld", "rule1", null, "Rule1", "SELF");
        businessValidationResults.addReviewNeeded(0, "fld", "rule2", new String[]{"Hello", "World"}, "Rule2");
        businessValidationResults.addReviewNeeded(0, "fld", "rule3", new String[]{"Hello", "World"}, "Rule3");
    }
}
