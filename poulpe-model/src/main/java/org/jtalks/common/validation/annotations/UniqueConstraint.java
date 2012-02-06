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
package org.jtalks.common.validation.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.jtalks.common.validation.unique.UniqueConstraintValidator;

/**
 * Annotation for declaring that in this class there is a constraint on field
 * uniqueness. For marking which fields should be used with {@link UniqueField}.<br>
 * <br>
 * 
 * Example of usage:
 * 
 * <pre>
 * &#064;UniqueConstraint
 * class Component extends Entity {
 *     &#064;UniqueField(message = "not.unique.name")
 *     private String name;
 *     &#064;UniqueField(message = "not.unique.type")
 *     private ComponentType componentType;
 *     // getters, setters
 * }
 * </pre>
 * 
 * 
 * @author Tatiana Birina
 * @author Alexey Grigorev
 * 
 * @see UniqueField
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueConstraintValidator.class)
@Documented
public @interface UniqueConstraint {
    /**
     * attribute message
     * 
     * @return error message
     */
    String message() default "field must be unique";

    /**
     * attribute groups specifies validation groups, to which this constraint
     * belongs
     */
    Class<?>[] groups() default {};

    /**
     * attribute payload assigns custom payload objects to a constraint
     */
    Class<? extends Payload>[] payload() default {};

}
