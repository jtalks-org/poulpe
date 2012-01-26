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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Type of the engine component.
 *
 * @author Pavel Vervenko
 * @see Component
 */
public enum ComponentType {
    FORUM, ARTICLE;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * This method loads default properties for the component.
     *
     * @return List<Property> list with default properties of the
     *         component
     */
    public List<Property> loadDefaults() {
        String componentType;
        if (this == FORUM) {
            componentType = "jcommune";
        } else {
            componentType = "antarcticle";
        }

        final String PATH = "org/jtalks/poulpe/service/config/" + componentType + ".properties";
        Resource propertiesFile = new ClassPathResource(PATH);

        Properties properties = new Properties();

        try {
            properties.load(propertiesFile.getInputStream());
        } catch (FileNotFoundException e) {
            logger.warn("Can't find file with default settings for " + this.name());
        } catch (IOException e) {
            logger.warn("IO Error while loading default settings for " + this.name());
        }

        return convertToList(properties);
    }

    private List<Property> convertToList(Properties properties) {
        List<Property> listProperties = new ArrayList<Property>();

        for (Object property : properties.keySet()) {
            String
                    name = property.toString(),
                    value = properties.get(property).toString();
            listProperties.add(new Property(name, value));
        }

        return listProperties;
    }

}
