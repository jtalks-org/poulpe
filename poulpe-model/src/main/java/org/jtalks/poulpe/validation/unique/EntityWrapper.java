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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jtalks.common.model.entity.Entity;
import org.jtalks.poulpe.validation.annotations.UniqueField;
import org.jtalks.poulpe.validation.util.AnnotatedField;
import org.jtalks.poulpe.validation.util.Refl;

/**
 * Allows manipulation with entity classes at runtime without knowing object of
 * what exact class it is, for getting values of those fields which are marked
 * with {@link UniqueField}
 * 
 * @author Alexey Grigorev
 */
class EntityWrapper {

    private final Entity object;
    private final Map<String, Object> properties;
    private final Map<String, String> errorsMessages;

    /**
     * Wraps the given entity, making possible to access to its values at
     * runtime without knowing instance of which class it is. Seeks for fields
     * marked with {@link UniqueField} annotation and stores their values in
     * internal map. <br>
     * <br>
     * 
     * If some of the annotated fields contain null, the will be excluded from
     * resulting set returned from {@link #getProperties()}, although
     * {@link #getValue(String)} will still return null.
     * 
     * @param entity being wrapped
     */
    public EntityWrapper(Entity entity) {
        object = entity;

        // can be moved away for providing caching (annotations and fields can't
        // be changed in runtime)
        List<AnnotatedField<UniqueField>> annotated = Refl.getAccessibleAnnotatedFields(entity.getClass(),
                UniqueField.class);

        List<Field> fields = new ArrayList<Field>();
        errorsMessages = new HashMap<String, String>();

        for (AnnotatedField<UniqueField> field : annotated) {
            fields.add(field.getField());
            errorsMessages.put(field.getFieldName(), field.getAnnotation().message());
        }
        //

        properties = Refl.convertToMapFilterNulls(entity, fields);
    }

    /**
     * @param fieldName
     * @return field's value
     */
    public Object getValue(String fieldName) {
        return properties.get(fieldName);
    }

    /**
     * @param fieldName
     * @return error message for the violated field
     */
    public String getErrorMessage(String fieldName) {
        return errorsMessages.get(fieldName);
    }

    /**
     * @return primary key for this entity
     */
    public long getEntityId() {
        return object.getId();
    }

    /**
     * @return the class of this entity
     */
    public Class<?> getEntityClass() {
        return object.getClass();
    }

    /**
     * @return set of all key-value properties (for fields, marked with
     * {@link UniqueField})
     */
    public Set<Entry<String, Object>> getProperties() {
        return properties.entrySet();
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, new String[] { "object" });
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, new String[] { "object" });
    }

}