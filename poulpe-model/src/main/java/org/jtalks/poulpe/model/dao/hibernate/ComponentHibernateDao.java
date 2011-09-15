/**
 * Copyright (C) 2011  jtalks.org Team
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
 * Also add information on how to contact you by electronic and paper mail.
 * Creation date: Apr 12, 2011 / 8:05:19 PM
 * The jtalks.org Project
 */
package org.jtalks.poulpe.model.dao.hibernate;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.jtalks.common.model.dao.hibernate.DefaultParentRepository;
import org.jtalks.poulpe.model.dao.ComponentDao;
import org.jtalks.poulpe.model.dao.DuplicatedField;
import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.model.entity.ComponentType;

/**
 * Implementation of dao for {@link Component}. The most part of methods were inherited from
 * superclass.
 * @author Pavel Vervenko
 */
public class ComponentHibernateDao extends DefaultParentRepository<Component> implements ComponentDao {

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Component> getAll() {
        return getSession().createQuery("from Component").list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<ComponentType> getAvailableTypes() {
        Set<ComponentType> result = new LinkedHashSet<ComponentType>(Arrays.asList(ComponentType.values()));
        for (Component current : getAll()) {
            result.remove(current.getComponentType());
        }
        return result;
    }
    
    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public Set<DuplicatedField> getDuplicateFieldsFor(Component component) {
        final String query = "FROM Component c WHERE (c.name = :name OR c.componentType = :type) AND c.id != :id";
        List<Component> list = getSession().createQuery(query)
                .setString("name", component.getName())
                .setInteger("type", figureOutType(component))
                .setLong("id", component.getId())
                .list();
        if (!list.isEmpty()) {
            Set<DuplicatedField> duplicates = new HashSet<DuplicatedField>();
            addAsDuplicationIfName(duplicates, list, component);
            addAsDuplicationIfType(duplicates, list, component);
            return duplicates;
        }
        return null;
    }

    /**
     * Returns the ordinal value for the component type to be compared with the values in the DB. As
     * far as {@link this#getDuplicateFieldsFor(Component)} is used for both user verification and
     * before-saving check it's necessary to be able to skip not input data.
     * @param component the component to be validate
     * @return the ordinal value for the component type or not valid ordinal value (
     *         {@link Integer#MIN_VALUE} if it's null
     */
    private int figureOutType(Component component) {
        if (component.getComponentType() == null) {
            return Integer.MIN_VALUE;
        } else {
            return component.getComponentType().ordinal();
        }
    }

    /**
     * Adds {@code ComponentDuplicateField#NAME} to the set of duplicates if {@code component}'s
     * name is a duplicate. It compares {@code component}'s name with the names of the components
     * from {@code list}.
     * @param duplicates the set of fields which will not be unique after adding component to the
     *            data source
     * @param list the list of components that have the same name or type as {@code component}
     * @param component the component who violates DB constraints.
     */
    private void addAsDuplicationIfName(Set<DuplicatedField> duplicates, List<Component> list,
            Component component) {
        for (Component item : list) {
            if (item.getName().equals(component.getName())) {
                duplicates.add(ComponentDuplicateField.NAME);
            }
        }
    }

    /**
     * Adds {@code ComponentDuplicateField#TYPE} to the set of duplicates if {@code component}'s
     * type is a duplicate. It compares {@code component}'s type with the types of the components
     * from {@code list}.
     * @param duplicates the set of fields which will not be unique after adding component to the
     *            data source
     * @param list the list of components that have the same name or type as {@code component}
     * @param component component who violates DB constraints.
     */
    private void addAsDuplicationIfType(Set<DuplicatedField> duplicates, List<Component> list,
            Component component) {
        for (Component item : list) {
            if (item.getComponentType().equals(component.getComponentType())) {
                duplicates.add(ComponentDuplicateField.TYPE);
            }
        }
    }
}
