/**
 * Copyright (C) 2011  JTalks.org Team
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jtalks.poulpe.validation;

import java.util.Set;

/**
 * Used for indicating that validation failed and that the entity being
 * validates has some constraint violations. To be used in services when saving
 * objects and to be thrown then premature validation somehow failed.<br>
 * <br>
 * 
 * Unchecked for not making noisy interfaces and services - and is to be handled
 * to by some exception handling dispatcher.
 * 
 * @author Alexey Grigorev
 */
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 692674710150875334L;

    private final Set<ValidationError> errors;

    /**
     * @param errorMessage
     * @param errors violated constraints
     */
    public ValidationException(String errorMessage, Set<ValidationError> errors) {
        super(errorMessage);
        this.errors = errors;
    }

    /**
     * @param errors violated constraints
     */
    public ValidationException(Set<ValidationError> errors) {
        this.errors = errors;
    }

    /**
     * @param result from which violated constraints is retrieved
     */
    public ValidationException(ValidationResult result) {
        this.errors = result.getErrors();
    }

    /**
     * @return the errors
     */
    public Set<ValidationError> getErrors() {
        return errors;
    }
}
