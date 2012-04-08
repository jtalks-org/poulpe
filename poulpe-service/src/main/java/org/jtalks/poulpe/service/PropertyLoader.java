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
package org.jtalks.poulpe.service;

import org.jtalks.common.model.entity.Component;
import org.jtalks.common.model.entity.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Responsible for loading properties for various types of components
 * 
 * @author Vahluev Vyacheslav
 */
public class PropertyLoader {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * A path to directory, where properties files are situated.
     */
    private String pathToProperties;

    /**
     * Gets the path to the directory with properties files
     * 
     * @return the String representation of path
     */
    public String getPathToProperties() {
        return pathToProperties;
    }

    /**
     * Sets the path to the directory with properties files
     * 
     * @param pathToProperties new path to set
     */
    public void setPathToProperties(String pathToProperties) {
        this.pathToProperties = pathToProperties;
    }

    /**
     * This method loads default properties for the component.
     * 
     * @param component component to load properties for
     */
    public void loadDefaults(Component component) {
        String componentType = component.getComponentType().toString().toLowerCase();
        String path = pathToProperties + componentType + ".properties";

        Resource propertiesFile = new ClassPathResource(path);
        Properties properties = new Properties();
        try {
            properties.load(propertiesFile.getInputStream());
        } catch (FileNotFoundException e) {
            logger.warn("Can't find file with default settings for " + componentType);
        } catch (IOException e) {
            logger.warn("IO Error while loading default settings for " + componentType);
        }

        component.setProperties(convertToList(properties));
    }

    /**
     * Converts {@link Properties} to List of {@link Property}
     * 
     * @param properties properties to convert
     * @return List of {@link Property}
     */
    public List<Property> convertToList(Properties properties) {
        List<Property> listProperties = new ArrayList<Property>();

        for (Map.Entry<Object, Object> property : properties.entrySet()) {
            String name = property.getKey().toString(), value = property.getValue().toString();
            listProperties.add(new Property(name, value));
        }

        return listProperties;
    }
}
