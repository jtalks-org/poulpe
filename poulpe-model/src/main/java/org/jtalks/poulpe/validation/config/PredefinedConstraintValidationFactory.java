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
package org.jtalks.poulpe.validation.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorFactory;

/**
 */
public class PredefinedConstraintValidationFactory implements ConstraintValidatorFactory {

    private Map<Class<?>, ConstraintValidator<?, ?>> validators;

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
        return (T) validators.get(key);
    }

    /**
     * @param validators the validators to set
     */
    public void setValidators(List<ConstraintValidator<?, ?>> validators) {
        Map<Class<?>, ConstraintValidator<?, ?>> index = new HashMap<Class<?>, ConstraintValidator<?, ?>>();
        for (ConstraintValidator<?, ?> cv : validators) {
            index.put(cv.getClass(), cv);
        }

        this.validators = index;
    }
    
}
