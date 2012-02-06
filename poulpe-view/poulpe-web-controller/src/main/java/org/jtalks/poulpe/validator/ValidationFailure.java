package org.jtalks.poulpe.validator;

import org.jtalks.common.validation.ValidationResult;

/**
 * 
 */
public interface ValidationFailure {
    
    /**
     * Shows validation errors
     * @param result the result of entity validation
     */
    void validationFailure(ValidationResult result);
    
}
