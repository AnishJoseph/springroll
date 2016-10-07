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
        if(!dtos.get(0).getClass().getSimpleName().contains("TestDTO"))return;
        businessValidationResults.addReviewNeeded(0, "fld", "message", null, "Rule1");
        businessValidationResults.addReviewNeeded(0, "fld", "message", null, null, "SELF");

    }
}
