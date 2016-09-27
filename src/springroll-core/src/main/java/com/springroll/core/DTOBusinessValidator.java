package com.springroll.core;

import java.util.List;

/**
 * All business validators must implement this interface. As events pass through the synchronous route
 * the business validator for the DTO is invoked. The method that implements this interface can modify
 * the DTOs passed to it.
 * The mapping of a DTO to its business validator is stored when the add method of JobDefinitions is invoked
 * and must be done at initialization.
 *
 *
 * Created by Anish Joseph on 12/09/16.
 * @since 1.0
 */
public interface DTOBusinessValidator {
    /**
     * The validate method
     * @param dtos
     * @param businessValidationResults
     * @param businessValidationDoneEarlier
     */
    void validate(List<? extends DTO> dtos, IBusinessValidationResults businessValidationResults, boolean businessValidationDoneEarlier);
}
