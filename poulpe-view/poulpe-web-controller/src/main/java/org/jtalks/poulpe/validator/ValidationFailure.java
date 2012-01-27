package org.jtalks.poulpe.validator;

import org.jtalks.poulpe.validation.ValidationResult;

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
