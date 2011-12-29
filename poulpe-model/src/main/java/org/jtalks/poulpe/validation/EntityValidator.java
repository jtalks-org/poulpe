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

import org.jtalks.common.model.entity.Entity;

/**
 * Class for validating objects.
 * 
 * @author Alexey Grigorev
 */
public abstract class EntityValidator {
    /**
     * Validates given object. If the object is valid, {@link #hasErrors()}
     * returns {@code false}. Otherwise, {@link #hasErrors()} returns true and
     * error messages can be obtained using {@link #getErrors()}
     * 
     * @param entity to be validated
     */
    public abstract ValidationResult validate(Entity entity);

    /**
     * Validates entity using {@link #validate(Entity)} and throws
     * {@link ValidationException} if any constraint violations are found.
     * 
     * @param entity to check
     * @throws ValidationException if any constraints violated
     */
    public void throwOnValidationFailure(Entity entity) throws ValidationException {
        ValidationResult result = validate(entity);

        if (result.hasErrors()) {
            throw new ValidationException(result);
        }
    }

}