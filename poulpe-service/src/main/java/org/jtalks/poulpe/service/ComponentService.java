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
package org.jtalks.poulpe.service;

import java.util.List;
import java.util.Set;

import org.jtalks.common.service.EntityService;
import org.jtalks.poulpe.model.dao.DuplicatedField;
import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.model.entity.ComponentType;
import org.jtalks.poulpe.service.exceptions.NotUniqueFieldsException;

/**
 * Service for some operations with {@link Component}.
 * 
 * @author Pavel Vervenko
 */
public interface ComponentService extends EntityService<Component> {

    /**
     * Get all components.
     * @return the list of the components
     */
    List<Component> getAll();

    /**
     * Delete the specified component.
     * @param component component to delete
     */
    void deleteComponent(Component component);

    /**
     * Save new or update existent component.
     * @param component component to save
     * @throws NotUniqueFieldsException when saving entity to the date source cause violations of DB constraints
     */
    void saveComponent(Component component) throws NotUniqueFieldsException;

    /**
     * Get the set of unoccupied ComponentType.
     * @return set of ComponentType
     */
    Set<ComponentType> getAvailableTypes();
    
    /**
     * Obtains the set of such fields which ought to be unique and whose uniqueness will be violated
     * after adding {@code component} to the data source.
     * @param component the component object
     * @return the set of fields whose uniqueness will be violated after adding {@code component}
     *         to the data source
     */
    Set<DuplicatedField> getDuplicateFieldsFor(Component component);
}
