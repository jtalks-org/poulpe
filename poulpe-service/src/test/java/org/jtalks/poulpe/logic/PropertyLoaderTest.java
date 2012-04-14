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
package org.jtalks.poulpe.logic;

import org.jtalks.common.model.entity.Property;
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
