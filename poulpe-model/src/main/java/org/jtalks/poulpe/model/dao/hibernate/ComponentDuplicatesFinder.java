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
package org.jtalks.poulpe.model.dao.hibernate;

import java.util.Collection;
import java.util.Set;

import org.jtalks.poulpe.model.dao.ComponentDao.ComponentDuplicateField;
import org.jtalks.poulpe.model.dao.DuplicatedField;
import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.model.entity.ComponentType;

import com.google.common.collect.Sets;

/**
 * Helper class for finding what fields for a given component violate
 * uniqueness constrains. Used in {@link ComponentHibernateDao} in
 * {@link ComponentHibernateDao#getDuplicateFieldsFor(Component)} method<br>
 * <br>
 * 
 * See {@link ComponentDuplicatesFinder#find()} for details on which types
 * may be found using this class.
 * 
 * @author Alexey Grigorev
 * @author Pavel Vervenko
 */
class ComponentDuplicatesFinder {

    private final Set<DuplicatedField> result = Sets.newHashSet();

    private final Component component;
    private final Collection<Component> duplicates;

    /**
     * Prepares the finder; for using call {@link #find()}. For details, see
     * {@link ComponentDuplicatesFinder} and {@link #find()}.
     * 
     * @param component which type and name is checked
     * @param list of components retrieved from the database with the same
     * name or type as the component
     * 
     * @see ComponentDuplicatesFinder
     * @see ComponentDuplicatesFinder#find()
     */
    public ComponentDuplicatesFinder(Component component, Collection<Component> duplicates) {
        this.component = component;
        this.duplicates = duplicates;
    }

    /**
     * Looks through the collection of {@link Component}s seeking for
     * violations of name and/or type uniqueness.<br>
     * <br>
     * 
     * Set made of the following elements is returned:<br>
     * - {@link ComponentDuplicateField#NAME} if the component has the same
     * name as another component already stored in the database;<br>
     * - {@link ComponentDuplicateField#TYPE} if the component has the same
     * type as another component.<br>
     * 
     * @return set of {@link DuplicatedField}
     */
    public Set<DuplicatedField> find() {
        lookForNameDuplicates();
        lookForTypeDuplicates();

        return result;
    }

    /**
     * Adds {@link ComponentDuplicateField#NAME} to the set of duplicates if
     * {@link #component}'s name is a duplicate.
     * 
     * It compares {@link #component}'s name with the names of the
     * components from {@link #duplicates}.
     */
    private void lookForNameDuplicates() {
        String name = component.getName();
        for (Component item : duplicates) {
            if (name.equals(item.getName())) {
                result.add(ComponentDuplicateField.NAME);
                break;
            }
        }
    }

    /**
     * Adds {@link ComponentDuplicateField#TYPE} to the set of duplicates if
     * {@link #component}'s type is a duplicate.
     * 
     * It compares {@link #component}'s type with the types of the
     * components from {@link #duplicates}.
     */
    private void lookForTypeDuplicates() {
        ComponentType type = component.getComponentType();

        for (Component item : duplicates) {
            if (type.equals(item.getComponentType())) {
                result.add(ComponentDuplicateField.TYPE);
                break;
            }
        }
    }
}

