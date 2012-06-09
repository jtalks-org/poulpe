package org.jtalks.poulpe.model.entity;

import org.jtalks.common.model.entity.Entity;
import org.jtalks.common.model.entity.Property;


/**
 * 
 * Usually only for reading
 * 
 * @author Alexey Grigorev
 *
 */
public class DefaultProperty extends Entity {
    
    private String name;
    private String value;
    private String validationRule;

    protected DefaultProperty() {
    }

    /**
     * Constructor which sets name and value of the property
     * 
     * @param name of the property
     * @param value of the property
     * @param validationRule for the value
     */
    public DefaultProperty(String name, String value, String validationRule) {
        this.name = name;
        this.value = value;
        this.validationRule = validationRule;
    }
    
    public Property toProperty() {
        Property property = new Property(name, value);
        property.setValidationRule(validationRule);
        return property;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValidationRule() {
        return validationRule;
    }

    public void setValidationRule(String validationRule) {
        this.validationRule = validationRule;
    }
}
