package com.springroll.mdm;

import com.springroll.core.DTO;
import com.springroll.core.DTOBusinessValidator;
import com.springroll.core.IBusinessValidationResults;
import com.springroll.core.SpringrollSecurity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by anishjoseph on 09/11/16.
 */
@Service
public class MdmBusinessValidator implements DTOBusinessValidator {
    @Override
    public void validate(List<? extends DTO> dtos, IBusinessValidationResults businessValidationResults) {
        MdmDTO mdmDTO = (MdmDTO)dtos.get(0);
        businessValidationResults.addReviewNeeded(0, "fld", "rule4", new String[]{mdmDTO.getMaster(), SpringrollSecurity.getUser().getUsername()}, "MdmMasterRule");
    }
}

