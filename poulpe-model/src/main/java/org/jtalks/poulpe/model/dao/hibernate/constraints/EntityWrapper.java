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

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.jtalks.common.model.entity.Entity;

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

    /**
     * Wraps the given entity, making possible to access to its values at
     * runtime without knowing instance of which class it is. Seeks for fields
     * marked with {@link UniqueField} annotation and stores their values in
     * internal map.
     * 
     * @param entity being wrapped
     */
    public EntityWrapper(Entity entity) {
        object = entity;
        List<Field> fields = Refl.getAccessibleAnnotatedFields(entity.getClass(), UniqueField.class);
        properties = Refl.convertToMap(entity, fields);
    }

    /**
     * @param fieldName
     * @return field's value
     */
    public Object getValue(String fieldName) {
        return properties.get(fieldName);
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

    /**
     * @return iterator for all key-value properties (for fields, marked with
     * {@link UniqueField})
     */
    public Iterator<Entry<String, Object>> getIterator() {
        return properties.entrySet().iterator();
    }

}