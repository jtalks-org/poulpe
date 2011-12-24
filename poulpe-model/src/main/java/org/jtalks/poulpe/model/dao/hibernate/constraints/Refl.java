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
package org.jtalks.poulpe.model.dao.hibernate.constraints;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for working with reflection, used when validating data stored
 * in entities.
 * 
 * @author Alexey Grigorev
 */
public class Refl {

    private Refl() {
    }

    /**
     * Makes all fields accessible by applying
     * {@link Field#setAccessible(boolean)} with {@code true} to them.
     * 
     * @param fields
     * @return the same fields, only accessible, so they can be used for
     * retrieving their data
     */
    public static List<Field> accessible(List<Field> fields) {
        for (Field field : fields) {
            field.setAccessible(true);
        }
        return fields;
    }

    /**
     * Retrieves all fields from the class, including its super classes,
     * filtering out only ones annotated by the given annotation. All fields
     * returned are accessible.
     * 
     * @param beanClass class whose fields will be retrieved
     * @param annotation class to look for
     * 
     * @return fields marked by the given annotation
     */
    public static <A extends Annotation> List<Field> getAccessibleAnnotatedFields(Class<?> beanClass,
            Class<A> annotation) {
        return accessible(getAnnotatedFields(beanClass, annotation));
    }

    /**
     * Retrieves all fields from the class, including its super classes,
     * filtering out only ones annotated by the given annotation.
     * 
     * @param beanClass class whose fields will be retrieved
     * @param annotation class to look for
     * 
     * @return fields marked by the given annotation
     */
    public static <A extends Annotation> List<Field> getAnnotatedFields(Class<?> beanClass, Class<A> annotation) {
        List<Field> fields = new ArrayList<Field>();

        for (Field field : getAllFields(beanClass)) {
            for (Annotation a : field.getAnnotations()) {
                if (annotation.isInstance(a)) {
                    fields.add(field);
                }
            }
        }

        return fields;
    }

    /**
     * Retrieves all fields from the class, including its super classes. <br>
     * 
     * <i>Leaves out all static fields.</i>
     * 
     * @param beanClass class whose fields will be extracted
     * @return list of extracted fields
     */
    public static List<Field> getAllFields(Class<?> beanClass) {
        List<Class<?>> allClasses = getClassHierarchy(beanClass);

        List<Field> fields = new ArrayList<Field>();

        for (Class<?> current : allClasses) {
            for (Field field : current.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                fields.add(field);
            }
        }

        return fields;
    }

    /**
     * Creates all class hierarchy from the given class to Object,
     * <b>exclusive</b>, i.e. Object won't go to the list). Interfaces won't be
     * listed in the result.
     * 
     * @param beanClass class for building its hierarchy
     * @return list of all classes from the given class up to {@link Object}
     */
    public static List<Class<?>> getClassHierarchy(Class<?> beanClass) {
        List<Class<?>> classes = new ArrayList<Class<?>>();

        Class<?> current = beanClass;
        while (current != Object.class) {
            classes.add(current);
            current = current.getSuperclass();
        }

        return classes;
    }

    /**
     * Converts an object to map using a collection of field instances.<br>
     * <br>
     * 
     * <b>Note:</b> Fields should be accessible (may be achieved using
     * {@link #accessible(List)})
     * 
     * @param object values from which will be extracted
     * @param fields a list of <b>accessible</b> fields
     * 
     * @return field names mapped to their values from the given bean
     * 
     * @exception IllegalArgumentException wrapping
     * {@link IllegalAccessException} when one of the fields inaccessible
     * @exception IllegalArgumentException if the bean is not an instance of the
     * class declaring the field (or a subclass or implementor)
     */
    public static Map<String, Object> convertToMap(Object object, List<Field> fields) {
        try {
            return convert(object, fields);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static Map<String, Object> convert(Object object, List<Field> fields) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<String, Object>();

        for (Field field : fields) {
            map.put(field.getName(), field.get(object));
        }

        return map;
    }

}
