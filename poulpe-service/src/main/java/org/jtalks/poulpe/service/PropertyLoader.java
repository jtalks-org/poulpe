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
import java.util.Properties;

/**
 * Responsible for loading properties for various types of components
 *
 * @author Vahluev Vyacheslav
 */
public class PropertyLoader {
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

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * This method loads default properties for the component.
     *
     * @param component component to load properties for
     */
    public void loadDefaults(Component component) {
        String componentType = component.getComponentType().toString().toLowerCase();
        final String PATH = pathToProperties + componentType + ".properties";

        Resource propertiesFile = new ClassPathResource(PATH);
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

        for (Object property : properties.keySet()) {
            String
                    name = property.toString(),
                    value = properties.get(property).toString();
            listProperties.add(new Property(name, value));
        }

        return listProperties;
    }
}
