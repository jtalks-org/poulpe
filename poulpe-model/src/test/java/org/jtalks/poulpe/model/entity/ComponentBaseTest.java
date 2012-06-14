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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.jtalks.common.model.entity.Property;
import org.jtalks.poulpe.test.fixtures.TestFixtures;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author Alexey Grigorev
 */
public class ComponentBaseTest {

    @Test(dataProvider = "componentTypes")
    public void newComponent_ofProperType(ComponentType componentType) {
        ComponentBase baseComponent = new ComponentBase(componentType);
        Component component = baseComponent.newComponent(random(), random());
        assertEquals(component.getComponentType(), baseComponent.getComponentType());
    }
    
    @DataProvider
    public Object[][] componentTypes() {
        return new Object[][] { { ComponentType.ADMIN_PANEL }, { ComponentType.ARTICLE }, { ComponentType.FORUM } };
    }
    
    @Test
    public void newComponent_nameAndDescription() {
        ComponentBase baseComponent = TestFixtures.baseComponent();
        String name = random(), description = random();
        
        Component component = baseComponent.newComponent(name, description);
        
        assertEquals(component.getName(), name);
        assertEquals(component.getDescription(), description);
    }
    
    @Test(expectedExceptions = IllegalStateException.class)
    public void newComponent_componentTypeNotSet() {
        ComponentBase baseComponent = new ComponentBase();
        baseComponent.newComponent(random(), random());
    }
    
    @Test
    public void newComponent_defaultPropertyCopied() {
        ComponentBase baseComponent = TestFixtures.baseComponent();
        Property property = givenDefaultProperty(baseComponent);
        
        Component component = baseComponent.newComponent(random(), random());
        
        assertContainsProperty(component, property);
    }

    private Property givenDefaultProperty(ComponentBase baseComponent) {
        Property property = TestFixtures.property();
        baseComponent.setDefaultProperties(Collections.singleton(property));
        return property;
    }
    
    private static void assertContainsProperty(Component component, Property property) {
        String actual = component.getProperty(property.getName());
        assertEquals(actual, property.getValue());
    }
    
    @Test
    public void newComponent_containsAllDefaultProperties() {
        ComponentBase baseComponent = TestFixtures.baseComponent();
        
        List<Property> properties = Arrays.asList(TestFixtures.property(), TestFixtures.property(), TestFixtures.property());
        baseComponent.setDefaultProperties(properties);
        
        Component component = baseComponent.newComponent(random(), random());
        
        assertEquals(component.getProperties().size(), properties.size());
    }
    
    @Test
    public void newComponent_propertiesAreNotTheSameEntities() {
        ComponentBase baseComponent = TestFixtures.baseComponent();
        Property property = givenDefaultProperty(baseComponent);
        
        Component component = baseComponent.newComponent(random(), random());
        
        assertPropertiesAreDifferentEntities(property, component);
    }

    private static void assertPropertiesAreDifferentEntities(Property property, Component component) {
        Property actual = component.getProperties().get(0);
        assertThat(actual, not(equalTo(property)));
    }
    
    private static String random() {
        return RandomStringUtils.randomAlphanumeric(10);
    }
}
