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

import org.jtalks.common.model.permissions.GeneralPermission;
import org.jtalks.common.service.EntityService;
import org.jtalks.poulpe.model.dto.PermissionChanges;
import org.jtalks.poulpe.model.dto.PermissionsMap;
import org.jtalks.poulpe.model.entity.ComponentBase;
import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.model.entity.ComponentType;

import java.util.List;
import java.util.Set;

/**
 * Service for some operations with {@link Component}.
 * 
 * @author Pavel Vervenko
 * @author Alexey Grigorev
 * @author Vyacheslav Zhivaev
 */
public interface ComponentService extends EntityService<Component> {

    /**
     * Get all components.
     * 
     * @return the list of the components
     */
    List<Component> getAll();

    /**
     * Delete the specified component.
     * 
     * @param component component to delete
     */
    void deleteComponent(Component component);

    /**
     * Saves new component or updates existent
     * 
     * @param component to save
     * @exception org.jtalks.common.validation.ValidationException when entity being saved violates validation
     * constraints
     */
    void saveComponent(Component component);

    /**
     * Get the set of unoccupied ComponentType.
     * 
     * @return set of ComponentType
     */
    Set<ComponentType> getAvailableTypes();

    /**
     * Gets component by it's type.
     * 
     * @param type the component's type
     * @return the component
     */
    Component getByType(ComponentType type);

    /**
     * Gets {@link PermissionsMap} for defined {@link Component}.
     * 
     * @param component the component to get for
     * @return {@link PermissionsMap} for defined {@link Component}
     */
    PermissionsMap<GeneralPermission> getPermissionsMapFor(Component component);

    /**
     * Change grants for component.
     * 
     * @see PermissionChanges
     * @param component the component to change for
     * @param changes the {@link PermissionChanges} which needs to be applied
     */
    void changeGrants(Component component, PermissionChanges changes);

    /**
     * Change restrictions for component.
     * 
     * @param component the component to change for
     * @param changes the {@link PermissionChanges} which needs to be applied
     */
    void changeRestrictions(Component component, PermissionChanges changes);

    /**
     * By given type retrieves {@link ComponentBase}
     * 
     * @param componentType of the component
     * @return base component of needed type
     */
    ComponentBase baseComponentFor(ComponentType componentType);

}
