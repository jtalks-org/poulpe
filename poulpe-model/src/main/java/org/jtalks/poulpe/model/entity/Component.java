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
import org.jtalks.poulpe.model.dao.hibernate.constraints.UniqueField;
import org.jtalks.poulpe.model.dao.hibernate.constraints.UniqueConstraint;

import ru.javatalks.utils.general.Assert;

/**
 * Represent jtalks engine component.
 * 
 * @author Pavel Vervenko
 */
@UniqueConstraint
public class Component extends Entity {

    @UniqueField
    private String name;
    
    private String description;
    
    @UniqueField
    private ComponentType componentType;

    /**
     * Default constructor, sets nothing - all values are nulls
     */
    public Component() {
    }

    public Component(String name, String description, ComponentType componentType) {
        this.name = name;
        this.description = description;
        this.componentType = componentType;
    }

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
     * @exception IllegalArgumentException if the description is null
     */
    public void setDescription(String description) {
        Assert.throwIfNull(description, "description");
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
     * @exception IllegalArgumentException if the name is null
     */
    public void setName(String name) {
        Assert.throwIfNull(name, "name");
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

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "Component [id=" + getId() + ", name=" + name + ", description=" + description + ", componentType="
                + componentType + "]";
    }
}
