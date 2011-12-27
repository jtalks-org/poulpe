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
import org.jtalks.poulpe.validation.annotations.UniqueConstraint;
import org.jtalks.poulpe.validation.annotations.UniqueField;

import ru.javatalks.utils.general.Assert;

/**
 * Represent jtalks engine component.
 * 
 * @author Pavel Vervenko
 */
@UniqueConstraint
public class Component extends Entity {
    // TODO: change it
    public static final String NOT_UNIQUE_NAME = "item.already.exist";
    public static final String NOT_UNIQUE_TYPE = "item.already.exist";
    
    @UniqueField(message = NOT_UNIQUE_NAME)
    private String name;
    
    private String description;
    
    @UniqueField(message = NOT_UNIQUE_TYPE)
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

    public Component(Component component) {
        this.name = component.getName();
        this.description = component.getDescription();
        this.componentType = component.getComponentType();
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
