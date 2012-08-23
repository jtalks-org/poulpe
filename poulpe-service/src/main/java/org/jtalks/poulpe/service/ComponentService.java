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
import org.jtalks.poulpe.model.entity.Component;
import org.jtalks.poulpe.model.entity.ComponentBase;
import org.jtalks.poulpe.model.entity.ComponentType;
import org.jtalks.poulpe.model.entity.Jcommune;
import org.jtalks.poulpe.service.exceptions.JcommuneRespondedWithErrorException;
import org.jtalks.poulpe.service.exceptions.JcommuneUrlNotConfiguratedException;
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
     * Delete the specified component.
     *
     * @param component component to delete
     * @throws {@link NoConnectionToJcommuneException}
     * @throws {@link JcommuneRespondedWithErrorException}
     * @throws {@link JcommuneUrlNotConfiguratedException}
     */
    void deleteComponent(Component component)
            throws NoConnectionToJcommuneException, JcommuneRespondedWithErrorException,
            JcommuneUrlNotConfiguratedException;

    /**
     * Saves new component or updates existent
     *
     * @param component to save
     * @throws org.jtalks.common.validation.ValidationException
     *          when entity being saved violates validation
     *          constraints
     */
    void saveComponent(Component component);

    /**
     * Runs re-index on  the specified component.
     *
     * @param jcommune component to re-index
     * @throws {@link NoConnectionToJcommuneException}
     * @throws {@link JcommuneRespondedWithErrorException}
     * @throws {@link JcommuneUrlNotConfiguratedException}
     */
    void reindexComponent(Jcommune jcommune)
            throws JcommuneRespondedWithErrorException, JcommuneUrlNotConfiguratedException,
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
