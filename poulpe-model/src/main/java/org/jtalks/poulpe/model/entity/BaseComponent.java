package org.jtalks.poulpe.model.entity;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.jtalks.common.model.entity.Property;

import com.google.common.collect.Lists;

/**
 * 
 * @author Alexey Grigorev
 *
 */
public class BaseComponent {

    private ComponentType componentType;
    private Collection<DefaultProperty> defaultProperties = Lists.newArrayList();

    public BaseComponent() {
    }
    
    public BaseComponent(ComponentType componentType) {
        this.componentType = componentType;
    }

    public ComponentType getComponentType() {
        return componentType;
    }

    public void setComponentType(ComponentType componentType) {
        this.componentType = componentType;
    }

    public Collection<DefaultProperty> getDefaultProperties() {
        return defaultProperties;
    }

    public void setDefaultProperties(Collection<DefaultProperty> defaultProperties) {
        this.defaultProperties = defaultProperties;
    }

    public Component newComponent(String name, String description) {
        Validate.validState(componentType != null, "componentType must be set");
        return componentType.newComponent(name, description, copy(defaultProperties));
    }
    
    private static List<Property> copy(Iterable<DefaultProperty> defaults) {
        List<Property> result = Lists.newArrayListWithExpectedSize(4);
        
        for (DefaultProperty property : defaults) {
            result.add(property.toProperty());
        }
        
        return result;
    }
}
