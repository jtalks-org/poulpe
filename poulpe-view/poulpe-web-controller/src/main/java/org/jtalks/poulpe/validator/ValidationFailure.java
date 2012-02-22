package org.jtalks.poulpe.validator;

import org.jtalks.common.validation.ValidationResult;

/**
 * Interface for view classes with input fields which may be violated.
 * If it's violated, {@link #validationFailure(ValidationResult)} is invoked
 * 
 * @author Alexey Grigorev
 */
public interface ValidationFailure {
    
    /**
     * Shows validation errors
     * @param result the result of entity validation
     */
    void validationFailure(ValidationResult result);
    
}
