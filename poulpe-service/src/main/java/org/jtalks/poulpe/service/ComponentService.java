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

import org.jtalks.common.service.EntityService;
import org.jtalks.common.model.entity.Component;
import org.jtalks.poulpe.model.entity.ComponentBase;
import org.jtalks.common.model.entity.ComponentType;
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.service.exceptions.EntityIsRemovedException;
import org.jtalks.poulpe.service.exceptions.EntityUniqueConstraintException;
import org.jtalks.poulpe.service.exceptions.JcommuneRespondedWithErrorException;
import org.jtalks.poulpe.service.exceptions.JcommuneUrlNotConfiguredException;
import org.jtalks.poulpe.service.exceptions.NoConnectionToJcommuneException;

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
     * Delete the specified component and notify it about the deletion of its content so that it can re-index its
     * database and do some clean-up.
     *
     * @param component component to delete and notify
     * @throws NoConnectionToJcommuneException
     *          if it wasn't possible to notify component about its removal because nothing was found by the URL
     *          specified in the configuration of the component (for instance because it was configured wrong)
     * @throws JcommuneRespondedWithErrorException
     *          if request reached some host but that host answered with some HTTP error (only error codes between 201
     *          and 299 are acceptable)
     * @throws JcommuneUrlNotConfiguredException if url to the component wasn't configured by admin
     * @throws EntityIsRemovedException if component was removed by another user
     *
     */
    void deleteComponent(Component component) throws NoConnectionToJcommuneException,
            JcommuneRespondedWithErrorException, JcommuneUrlNotConfiguredException, EntityIsRemovedException;

    /**
     * Saves new component or updates existent
     *
     * @param component to save
     * @throws org.jtalks.common.validation.ValidationException
     *          when entity being saved violates validation constraints
     * @deprecated addComponent and updateComponent are used 
     */
    void saveComponent(Component component);
    
    /**
     * Saves new component.
     * Use instead of {@link saveComponent}.
     * 
     * @param component to save
     * @throws ValidationException when entity being saved violates validation constraints
     * @throws EntityUniqueConstraintException if component with the same {@link ComponentType} already exists in database
     */
    void addComponent(Component component) throws EntityUniqueConstraintException;

    /**
     * Update existed component.
     * Use instead of {@link saveComponent}.
     * 
     * @param component to update
     * @throws ValidationException when entity being saved violates validation constraints
     * @throws EntityIsRemovedException if the component was removed from database by another user
     */
    void updateComponent(Component component) throws EntityIsRemovedException;

    /**
     * Runs re-index on  the specified component.
     *
     * @param jcommune component to re-index
     * @throws {@link NoConnectionToJcommuneException}
     * @throws {@link JcommuneRespondedWithErrorException}
     * @throws {@link org.jtalks.poulpe.service.exceptions.JcommuneUrlNotConfiguredException}
     */
    void reindexComponent(Jcommune jcommune)
            throws JcommuneRespondedWithErrorException, JcommuneUrlNotConfiguredException,
            NoConnectionToJcommuneException;

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
     * By given type retrieves {@link ComponentBase}
     *
     * @param componentType of the component
     * @return base component of needed type
     */
    ComponentBase baseComponentFor(ComponentType componentType);

}
