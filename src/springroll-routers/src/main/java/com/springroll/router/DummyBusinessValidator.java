package com.springroll.router;

import com.springroll.core.DTO;
import com.springroll.core.DTOBusinessValidator;
import com.springroll.core.IBusinessValidationResults;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by anishjoseph on 05/11/16.
 */
@Service
public class DummyBusinessValidator implements DTOBusinessValidator {
    @Override
    public void validate(List<? extends DTO> dtos, IBusinessValidationResults businessValidationResults) {
    }
}

