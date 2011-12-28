package org.jtalks.common.security;


import org.jtalks.common.model.entity.Entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to map DTO to corresponding model class.
 * Date: 16.09.2011<br />
 * Time: 17:02
 *
 * @author Alexey Malev
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ModelEntity {
    /**
     * Entity class is mapped to with the annotation. Default is {@link Entity}
     */
    Class<?> value() default Entity.class;
}

