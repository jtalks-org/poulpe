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
package org.jtalks.common.validation;

import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.jtalks.common.model.entity.Entity;

/**
 * Implementation of {@link EntityValidator} using JSR-303. It delegates the
 * verification to JSR-303 validator and then wraps its response to more
 * convenient form.
 * 
 * @author Alexey Grigorev
 */
public class Jsr303EntityValidator extends EntityValidator {

    private Validator validator;

    /** {@inheritDoc} */
    @Override
    public ValidationResult validate(Entity o) {
        Set<ValidationError> errorSet = wrap(validator.validate(o));
        return new ValidationResult(errorSet);
    }

    /**
     * Converts JSR-303 set of {@link ConstraintViolation} to our {@link ValidationError} objects
     * @param set to be converted
     * @return converted set
     */
    private static Set<ValidationError> wrap(Set<ConstraintViolation<Entity>> set) {
        Set<ValidationError> result = new HashSet<ValidationError>();

        for (ConstraintViolation<?> c : set) {
            result.add(new ValidationError(c.getPropertyPath().toString(), c.getMessage()));
        }

        return result;
    }

    /**
     * @return the validator
     */
    public Validator getValidator() {
        return validator;
    }

    /**
     * @param validator the validator to set
     */
    public void setValidator(Validator validator) {
        this.validator = validator;
    }

}
