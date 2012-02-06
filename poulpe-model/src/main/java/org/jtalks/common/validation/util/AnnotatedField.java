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
package org.jtalks.common.validation.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * A pair of field and its annotation
 * 
 * @author Alexey Grigorev
 */
public class AnnotatedField<A extends Annotation> {
    private final Field field;
    private final A annotation;

    /**
     * Creates a pair of field and its annotation
     * 
     * @param field with annotation
     * @param annotation
     */
    public AnnotatedField(Field field, A annotation) {
        this.field = field;
        this.annotation = annotation;
    }

    /**
     * @return the field
     */
    public Field getField() {
        return field;
    }

    /**
     * @return field name
     */
    public String getFieldName() {
        return field.getName();
    }

    /**
     * @return the annotation
     */
    public A getAnnotation() {
        return annotation;
    }
}