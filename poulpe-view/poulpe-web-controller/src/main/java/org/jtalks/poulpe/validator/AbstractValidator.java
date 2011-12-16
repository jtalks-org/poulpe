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
    protected void setError(String errorMessage) {
        this.error = true;
        this.errorMessage = errorMessage;
    }
    
    @Override
    public boolean hasError() {
        return error;
    }

    @Override
    public String getError() {
        return errorMessage;
    }
    
}
