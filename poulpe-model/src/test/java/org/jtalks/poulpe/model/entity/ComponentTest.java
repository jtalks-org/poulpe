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
import static org.testng.Assert.assertNotSame;
import static org.testng.Assert.assertNull;

import org.jtalks.common.model.entity.Component;
import org.jtalks.common.model.entity.ComponentType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

// TODO: Do we really need get-set test? 
public class ComponentTest {
    Component component;

    @BeforeMethod
    public void setUp() {
        component = new Component();
    }

    @Test
    public void testGetDescription() {
        String description = "blahblahblah";
        component.setDescription(description);
        assertEquals(component.getDescription(), description);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetDescription() {
        String description = "blahblahblah";
        component.setDescription(description);
        assertEquals(component.getDescription(), description);
        component.setDescription(null);
    }

    @Test
    public void testGetName() {
        String name = "blahblahblah";
        component.setDescription(name);
        assertEquals(component.getDescription(), name);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetName() {
        component.setName(null);
    }

    @Test
    public void testGetComponentTypeArticle() {
        assertNull(component.getComponentType());
        component.setComponentType(ComponentType.ARTICLE);
        assertEquals(ComponentType.ARTICLE, component.getComponentType());
        assertNotSame(ComponentType.FORUM, component.getComponentType());
    }

    @Test
    public void testGetComponentTypeForum() {
        assertNull(component.getComponentType());
        component.setComponentType(ComponentType.FORUM);
        assertEquals(ComponentType.FORUM, component.getComponentType());
        assertNotSame(ComponentType.ARTICLE, component.getComponentType());
    }

    @Test
    public void testAddProperty() {
        component.addProperty("someprop", "someval");

        assertEquals(component.getProperties().size(), 1);
        assertEquals(component.getProperties().get(0).getName(), "someprop");
        assertEquals(component.getProperties().get(0).getValue(), "someval");
    }

    @Test
    public void testSetProperty() {
        component.addProperty("setname", "setval");
        component.setProperty("setname", "x");

        assertEquals(component.getProperties().size(), 1);
        assertEquals(component.getProperties().get(0).getName(), "setname");
        assertEquals(component.getProperties().get(0).getValue(), "x");
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
        component.addProperty("setname", "setval");

        assertEquals(component.getProperty("unknown"), null);
    }

    @Test
    public void testToString() {
        String
                expected = "Component [id=" + 0 + ", name=" + component.getName() + ", description="
                            + component.getDescription() + ", componentType=" + component.getComponentType() + "]",
                returned = component.toString();
        assertEquals(expected, returned);
    }

}
