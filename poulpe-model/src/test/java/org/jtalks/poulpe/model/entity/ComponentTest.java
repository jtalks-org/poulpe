package org.jtalks.poulpe.model.entity;

import static org.testng.Assert.*;

import junit.framework.TestCase;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ComponentTest {
    Component component;
    @BeforeMethod
    public void setUp(){
        component = new Component();
    }
    @Test
    public void testGetDescription(){
        String description =  "blahblahblah";
        component.setDescription(description);
        assertEquals(component.getDescription(), description);
    }
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetDescription(){
        String description =  "blahblahblah";
        component.setDescription(description);
        assertEquals(component.getDescription(), description);
        component.setDescription(null);
        }
    @Test
    public void testGetName(){
         String name = "blahblahblah";
        component.setDescription(name);
        assertEquals(component.getDescription(), name);
    }
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetName(){
        component.setName(null);
    }
    @Test
    public void testGetComponentTypeArticle(){
        assertNull(component.getComponentType());
        component.setComponentType(ComponentType.ARTICLE);
        assertEquals(ComponentType.ARTICLE, component.getComponentType());
        assertNotSame(ComponentType.FORUM, component.getComponentType());
    }

    @Test
    public void testGetComponentTypeForum(){
        assertNull(component.getComponentType());
        component.setComponentType(ComponentType.FORUM);
        assertEquals(ComponentType.FORUM, component.getComponentType());
        assertNotSame(ComponentType.ARTICLE, component.getComponentType());
    }

}
