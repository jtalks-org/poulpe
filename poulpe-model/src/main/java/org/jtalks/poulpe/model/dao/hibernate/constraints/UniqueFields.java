package org.jtalks.poulpe.model.dao.hibernate.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;


@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueConstraintValidator.class)
@Documented
public @interface UniqueFields {
	/**
	 * attribute message
	 * 
	 * @return error message
	 */
	String message() default "field {field} should be unique";

	String[] fields();
	
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
