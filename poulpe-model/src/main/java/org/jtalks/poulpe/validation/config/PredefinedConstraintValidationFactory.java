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
 * Factory which returns the same instance of validator, defined via its
 * {@link PredefinedConstraintValidationFactory#setValidators(List)}<br>
 * <br>
 * 
 * <b>Note</b>: this implementation is <b>NOT THREAD SAFE</b> when used
 * incorretly. It will return <b>exactly the same instance</b> each time a
 * validator requested - thus <b>they must not keep any state</b>. For safe
 * implementation, see {@link ContextAwareConstraintValidatorFactory} or the
 * default spring's one. The reason why it isn't used is, when creating a bean,
 * autowiring leads to tons of warnings from zk-beans due to their incorrect
 * implementation of some specific spring features.
 */
public class PredefinedConstraintValidationFactory implements ConstraintValidatorFactory {

    private Map<Class<?>, ConstraintValidator<?, ?>> validators;

    /**
     * Returns one of the validators passed via {@link #setValidators(List)} or
     * null, if there's none appropriate
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
        return (T) validators.get(key);
    }

    /**
     * Pay attention to the note at
     * {@link PredefinedConstraintValidationFactory} and don't put in the list
     * any validators which can have any state.
     * 
     * @param validators
     */
    public void setValidators(List<ConstraintValidator<?, ?>> validators) {
        Map<Class<?>, ConstraintValidator<?, ?>> index = new HashMap<Class<?>, ConstraintValidator<?, ?>>();
        for (ConstraintValidator<?, ?> cv : validators) {
            index.put(cv.getClass(), cv);
        }

        this.validators = index;
    }

}
