package org.jtalks.poulpe.validator;

/**
 * Class for validating objects.
 * 
 * @author Alexey Grigorev
 */
public interface Validator<E> {
    /**
     * Validates given object. If the object is valid, {@link #hasError()} returns {@code false}.
     * Otherwise, {@link #hasError()} returns true and error message can be obtained using
     * {@link #getError()}
     * 
     * @param e object to be validated
     */
    void validate(E e);
    
    /**
     * Should be called only after {@link #validate(Object)}
     * @return {@code false} if there's no error, {@code true} otherwise
     */
    boolean hasError();
    
    /**
     * Should be called only after {@link #validate(Object)}. Before calling,
     * make sure that there is a error by invoking {@link #hasError()}.
     * @return specified error message
     * @exception IllegalStateException if actually there is no error
     */
    String getError();
}