package com.springroll.core;

import java.util.List;

/**
 * All business validators must implement this interface. As events pass through the synchronous route
 * the business validator for the DTO is invoked.
 *
 * Created by Anish Joseph on 12/09/16.
 * @since 1.0
 */
public interface DTOBusinessValidator {
    /**
     * The validate method
     * @param dtos
     * @param businessValidationResults
     */
    void validate(List<? extends DTO> dtos, IBusinessValidationResults businessValidationResults);
}
