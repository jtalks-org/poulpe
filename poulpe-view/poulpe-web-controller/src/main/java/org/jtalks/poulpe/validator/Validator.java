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