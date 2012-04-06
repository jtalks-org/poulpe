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
package org.jtalks.poulpe.web.controller.component.items;

import java.util.Set;

import org.jtalks.common.model.entity.ComponentType;

/**
 * Represents {@link org.jtalks.common.model.entity.Component Component}'s data. Implementation should refresh the view
 * when set methods are invoked
 * 
 * @author Dmitriy Sukharev
 * @author Alexey Grigorev
 */
public interface ItemDataView {

    /**
     * @return component's id
     */
    long getComponentId();

    /**
     * Sets the component's id, refreshing the view accordingly.
     * @param componentId the new id
     */
    void setComponentId(long componentId);

    /**
     * @return value from the name textbox.
     */
    String getName();

    /**
     * Sets the component's name, refreshing the name textbox.
     * @param name the new name
     */
    void setName(String name);

    /**
     * @return value from description textbox.
     */
    String getDescription();

    /**
     * Sets the component's description, refreshing the description textbox.
     * @param description the new description.
     */
    void setDescription(String description);

    /**
     * @return currently selected value from component's type listbox.
     */
    ComponentType getComponentType();

    /**
     * Sets the component's type, refreshing the type listbox.
     * @param type the new component type
     */
    void setComponentType(ComponentType type);

    /**
     * Sets the list of available component types for the current item,
     * refreshes the items in the listbox.
     * @param types the list of available component types
     */
    void setComponentTypes(Set<ComponentType> types);
}
