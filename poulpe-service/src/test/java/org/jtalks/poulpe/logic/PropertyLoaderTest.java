package org.jtalks.poulpe.logic;

import org.jtalks.poulpe.model.entity.Property;
import org.jtalks.poulpe.service.PropertyLoader;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @author Vahluev Vyacheslav
 */
public class PropertyLoaderTest {
    PropertyLoader propertyLoader;

    @BeforeMethod
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        propertyLoader = new PropertyLoader();
    }

    public List<Property> listProperties() {
        return Arrays.asList(new Property("k1", "v1"));
    }

    public Properties tableProperties() {
        Properties p = new Properties();
        p.put("k1", "v1");

        return p;
    }

    @Test
    public void testConvertToList() {
        List<Property> l = listProperties();

        List<Property> converted = propertyLoader.convertToList(tableProperties());

        assertTrue(converted.size() > 0);
        assertEquals(converted.get(0).getName(), l.get(0).getName());
        assertEquals(converted.get(0).getValue(), l.get(0).getValue());
    }
}
