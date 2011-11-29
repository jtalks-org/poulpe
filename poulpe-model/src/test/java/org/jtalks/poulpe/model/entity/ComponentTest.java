package org.jtalks.poulpe.model.entity;

import static org.testng.Assert.*;

import junit.framework.TestCase;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Necros
 * Date: 29.11.11
 * Time: 13:51
 * To change this template use File | Settings | File Templates.
 */

public class ComponentTest extends TestCase{
    Component component;

    public void setUp(){
        component = new Component();
    }
    public void testGetDescription(){
        String description =  "blahblahblah";
        component.setDescription(description);
        assertEquals(component.getDescription(),description);
    }
    public void testSetDescription(){
        String description =  "blahblahblah";
        component.setDescription(description);
        assertEquals(component.getDescription(),description);
        try{
        component.setDescription(null);
            fail("No exception");
        }catch (IllegalArgumentException ex){}
        }
    public void testGetName(){
         String name = "blahblahblah";
        component.setDescription(name);
        assertEquals(component.getDescription(),name);
    }
    public void testSetName(){
        try{
            component.setName(null);
            fail("No exception");
        } catch (IllegalArgumentException ex){}
    }
    public void testGetComponentType(){
        assertNull(component.getComponentType());
        component.setComponentType(ComponentType.ARTICLE);
        assertEquals(ComponentType.ARTICLE,component.getComponentType());
        assertNotSame(ComponentType.FORUM,component.getComponentType());
        component.setComponentType(ComponentType.FORUM);
        assertEquals(ComponentType.FORUM,component.getComponentType());
        assertNotSame(ComponentType.ARTICLE,component.getComponentType());
    }

}
