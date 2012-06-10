package org.jtalks.poulpe.model.entity;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.jtalks.common.model.entity.Property;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * 
 * @author Alexey Grigorev
 */
public class BaseComponent {

    private ComponentType componentType;
    private Collection<Property> defaultProperties = Sets.newLinkedHashSet();

    /**
     * Visible for hibernate
     */
    protected BaseComponent() {
    }
    
    /**
     * 
     * @param componentType
     */
    public BaseComponent(ComponentType componentType) {
        this.componentType = componentType;
    }

    /**
     * Based on current component type, creates a component of this type 
     * and fills it with default properties.
     * 
     * @param name of the component
     * @param description its description
     * @return component of needed type
     */
    public Component newComponent(String name, String description) {
        Validate.validState(componentType != null, "componentType must be set");
        return componentType.newComponent(name, description, copyAll(defaultProperties));
    }
    
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
     * @param defaultProperties collection of default properties
     */
    protected void setDefaultProperties(Collection<Property> defaultProperties) {
        this.defaultProperties = defaultProperties;
    }
}
