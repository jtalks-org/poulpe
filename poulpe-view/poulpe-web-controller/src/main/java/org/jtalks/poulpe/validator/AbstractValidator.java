package org.jtalks.poulpe.validator;

/**
 * Base implementation for {@link Validator}. Implements {@link #hasError()} and {@link #getError()}
 * leaving {@link #validate(Object)} for specific implementation.
 * 
 * @author Alexey Grigorev
 */
public abstract class AbstractValidator<E> implements Validator<E> {
    
    private boolean error = false;
    private String errorMessage = null;
    
    /**
     * Sets an error message. Should be called from child classes when overriding {@link Validator#validate(Object)}.
     */
    protected void addError(String errorMessage) {
        this.error = true;
        this.errorMessage = errorMessage;
    }
    
    @Override
    public boolean hasError() {
        return error;
    }

    @Override
    public String getError() {
        if (!error) {
            throw new IllegalStateException("Validator has no errors");
        }
        
        return errorMessage;
    }
    
}
