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
package org.jtalks.poulpe.validation.unique;

import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import javax.validation.ConstraintViolation;

import org.jtalks.poulpe.validation.annotations.UniqueConstraint;
import org.jtalks.poulpe.validation.annotations.UniqueField;

/**
 * Helper class for using in {@link UniqueConstraintValidator}. It was extracted
 * out for refactoring purposes and now is used for checking if the given entity
 * violates uniqueness of fields set by {@link UniqueConstraint} annotation.<br>
 * <br>
 * 
 * It is used after the list of fields, whose uniqueness are violated, is
 * retrieved - and looks through the whole list checking which field exactly was
 * violated, adding new nodes to {@link ConstraintViolationBuilder}
 * 
 * @author Alexey Grigorev
 * @author Tatiana Birina
 * 
 * @see UniqueConstraintValidator
 * @see UniquenessViolatorsRetriever
 * @see UniqueConstraint
 * @see UniqueField
 */
class UniquenessViolationFinder {

    private final EntityWrapper bean;
    private List<EntityWrapper> duplicates = Collections.emptyList();

    /**
     * Creates an instance of {@link UniquenessViolationFinder}. Usage:
     * 
     * <pre>
     * forEntity(entity).in(duplicates).findViolationsAndAddTo(context);
     * </pre>
     * 
     * @param entity the candidate for violating uniqueness
     * @see UniquenessViolationFinder
     */
    public static UniquenessViolationFinder forEntity(EntityWrapper entity) {
        return new UniquenessViolationFinder(entity);
    }

    /**
     * Creates an instance saving the bean for further using. See
     * {@link #forEntity(EntityWrapper)} and use it instead of this constructor.
     */
    UniquenessViolationFinder(EntityWrapper bean) {
        this.bean = bean;
    }

    /**
     * Defines in which list the violated fields will be looked for
     * 
     * @param duplicates recently retrieved list of beans
     * @return this instance for on-going calls
     */
    public UniquenessViolationFinder in(List<EntityWrapper> duplicates) {
        this.duplicates = duplicates;
        return this;
    }

    /**
     * Determines whether the given entity violates the uniqueness of values
     * retrieved from the database.<br>
     * <br>
     * 
     * Goes through the given list of beans looking for values whose uniqueness
     * are violated. When finds one, adds the name of the violated fields using
     * {@link ConstraintViolationBuilder} obtained from
     * {@link ConstraintValidatorContext}, so afterwards (after the validation
     * process) it will be placed onto resulted set of
     * {@link ConstraintViolation} objects.<br>
     * <br>
     * 
     * Also disables default constraint violation, so there will be only
     * manually added violations.
     * 
     * @param context to which constraint violation will be added
     * @return {@code true} if it doesn't violate the uniqueness, {@code false}
     * otherwise
     */
    public void findViolationsAndAddTo(ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        for (Entry<String, Object> entry : bean.getProperties()) {
            String fieldName = entry.getKey();
            Object value = entry.getValue();

            for (EntityWrapper duplicate : duplicates) {
                Object toCompare = duplicate.getValue(fieldName);

                if (value.equals(toCompare)) {
                    addErrorMessage(context, fieldName);
                    break;
                }
            }
        }
    }

    private void addErrorMessage(ConstraintValidatorContext context, String fieldName) {
        String errorMessage = bean.getErrorMessage(fieldName);
        ConstraintViolationBuilder builder = context.buildConstraintViolationWithTemplate(errorMessage);
        builder.addNode(fieldName).addConstraintViolation();
    }

}