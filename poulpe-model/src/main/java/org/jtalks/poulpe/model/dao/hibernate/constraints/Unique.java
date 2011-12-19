package org.jtalks.poulpe.model.dao.hibernate.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.jtalks.common.model.entity.Entity;

@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueConstraintValidator.class)
@Documented
public @interface Unique {
	/**
	 * attribute message
	 * 
	 * @return error message
	 */
	String message() default "{ field should be unique.}";

	Class<?> entity();

	String field();

	/**
	 * attribute groups
	 * specifies validation groups, to which this constraint belongs
	 */
	Class<?>[] groups() default {};

	/**
	 * attribute payload assigns custom payload objects to a constraint
	 */
	Class<? extends Payload>[] payload() default {};

}
