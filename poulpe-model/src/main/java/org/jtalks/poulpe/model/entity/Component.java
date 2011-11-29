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
package org.jtalks.poulpe.model.entity;

import org.jtalks.common.model.entity.Entity;

/**
 * Represent jtalks engine component.
 * 
 * @author Pavel Vervenko
 */
public class Component extends Entity {

    private String name;
    private String description;
    private ComponentType componentType;

    /**
     * Get the component description.
     * @return description
     */
    public String getDescription() {
        return description;
    }
    /**
     * Set components description.
     * @param description 
     */
    public void setDescription(String description) {
        if (description == null){
            throw new IllegalArgumentException();
        }
        this.description = description;
    }

    /**
     * Get component's name.
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the component.
     * @param name 
     */
    public void setName(String name) {
        if (name == null){
            throw new IllegalArgumentException();
        }
        this.name = name;
    }

    /**
     * Get the type of the component.
     * @return type
     */
    public ComponentType getComponentType() {
        return componentType;
    }

    /**
     * Set component's type.
     * @param type 
     */
    public void setComponentType(ComponentType type) {
        this.componentType = type;
    }
}
