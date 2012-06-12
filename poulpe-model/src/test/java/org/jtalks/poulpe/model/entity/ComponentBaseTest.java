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
