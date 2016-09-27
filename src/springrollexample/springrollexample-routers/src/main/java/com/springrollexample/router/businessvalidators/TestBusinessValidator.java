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
    public void validate(List<? extends DTO> dtos, IBusinessValidationResults businessValidationResults, boolean businessValidationDoneEarlier) {
        if(!businessValidationDoneEarlier) businessValidationResults.addReviewNeeded("Rule1", "Grp1");

    }
}
