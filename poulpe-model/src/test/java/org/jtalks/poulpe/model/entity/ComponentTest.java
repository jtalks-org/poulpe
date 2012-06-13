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

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.jtalks.common.model.entity.Property;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * 
 * @author unascribed
 * @author Alexey Grigorev
 */
public class ComponentTest {
    
    Component component;

    @BeforeMethod
    public void setUp() {
        component = new Component();
    }

    @Test
    public void addProperty_addedToList_listSize() {
        component.addProperty("someprop", "someval");

        List<Property> properties = component.getProperties();
        assertEquals(properties.size(), 1);
    }
    
    @Test
    public void addProperty_addedToList() {
        String name = random(), value = random();
        
        component.addProperty(name, value);

        Property property = firstProperty(component);
        
        assertNeededProperty(name, value, property);
        
    }

    private Property firstProperty(Component component) {
        List<Property> properties = component.getProperties();
        return properties.get(0);
    }

    private static void assertNeededProperty(String name, String value, Property property) {
        assertEquals(property.getName(), name);
        assertEquals(property.getValue(), value);
    }

    @Test
    public void testSetProperty() {
        String name = "someprop", value = "someval";
        
        component.addProperty(name, random());
        component.setProperty(name, value);

        assertEquals(component.getProperties().get(0).getName(), name);
        assertEquals(component.getProperties().get(0).getValue(), value);
    }

    @Test
    public void testSetPropertyWithAdd() {
        component.addProperty("setname", "setval");
        component.setProperty("setname2", "x");

        assertEquals(component.getProperties().size(), 2);
        assertEquals(component.getProperties().get(1).getName(), "setname2");
        assertEquals(component.getProperties().get(1).getValue(), "x");
    }

    @Test
    public void testGetProperty() {
        component.addProperty("setname", "setval");
        assertEquals(component.getProperty("setname"), "setval");
    }

    @Test
    public void testGetPropertyNull() {
        assertEquals(component.getProperty("unknown"), null);
    }

    private static String random() {
        return RandomStringUtils.randomAlphanumeric(10);
    }
}
