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

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.jtalks.common.model.entity.Property;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * "Template" for producing components. It stores default properties and, when creating a component, copies all of them
 * to a new component. For each {@link ComponentType} there must be only one {@link ComponentType}.<br>
 * <br>
 * 
 * If adding a new value to {@link ComponentType} enum, make sure that corresponding {@link ComponentBase} entity is
 * created.
 * 
 * @author Alexey Grigorev
 * @see ComponentType
 * @see Component
 */
public class ComponentBase {

    private ComponentType componentType;
    private Collection<Property> defaultProperties = Sets.newLinkedHashSet();

    /**
     * Visible for hibernate
     */
    protected ComponentBase() {
    }

    /**
     * Constructs {@link ComponentBase} with given {@link ComponentType}. Typically shouldn't be invoked manually -
     * because all {@link ComponentBase} entities should in the database already.
     * 
     * @param componentType type for the component
     */
    public ComponentBase(ComponentType componentType) {
        this.componentType = componentType;
    }

    /**
     * Based on current component type, creates a component of this type and fills it with default properties.
     * 
     * @param name of the component
     * @param description its description
     * @return component of needed type
     */
    public Component newComponent(String name, String description) {
        Validate.validState(componentType != null, "componentType must be set");
        return componentType.newComponent(name, description, copyAll(defaultProperties));
    }

    /**
     * Ensures that properties are cloned, not used by reference
     * 
     * @param defaults properties
     * @return list of cloned properties
     */
    private static List<Property> copyAll(Iterable<Property> defaults) {
        List<Property> result = Lists.newArrayListWithExpectedSize(4);

        for (Property property : defaults) {
            result.add(copy(property));
        }

        return result;
    }

    private static Property copy(Property property) {
        Property copy = new Property(property.getName(), property.getValue());
        copy.setValidationRule(property.getValidationRule());
        return copy;
    }

    /**
     * @return component type of this base component
     */
    public ComponentType getComponentType() {
        return componentType;
    }

    /**
     * Visible for hibernate
     * 
     * @param componentType type of the component
     */
    protected void setComponentType(ComponentType componentType) {
        this.componentType = componentType;
    }

    /**
     * @return default properties
     */
    public Collection<Property> getDefaultProperties() {
        return defaultProperties;
    }

    /**
     * Visible for hibernate
     * 
     * @param defaultProperties collection of default properties
     */
    protected void setDefaultProperties(Collection<Property> defaultProperties) {
        this.defaultProperties = defaultProperties;
    }
}
