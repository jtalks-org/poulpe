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
package org.jtalks.poulpe.web.listener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;
import org.jtalks.poulpe.model.utils.JndiAwarePropertyPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import com.google.common.annotations.VisibleForTesting;


/**
 * Application startup listener which initialize log4j from external or embedded configuration file. 
 * <ol>
 *  <li>It allows to configure our environments without unpacking and changing log4j inside of war file </li>
 *  <li>It allows to change logs location as well as configuration depending on environment</li>
 *  <li>It allows for instance on PROD disable default {@code DEBUG} level turned on</li>
 * </ol>
 * <p>This listener should be registered and started before any other servlet/listener (which use logging). So it 
 * should starts before usual logger initialization.</p>
 * <p>Logger search configuration file in common order: 
 * <ol>
 *  <li>JNDI</li>
 *  <li>datasource.properties file</li>
 *  <li>system properties</li>
 *  <li>embedded</li>
 * </ol></p>
 * <p>We can't use Spring IoC, because order of bean initialization is not managed and some classes can start using 
 * logger which configured differently.</p>
 *
 * @author Evgeny Kapinos
 * @see <a href="http://logging.apache.org/log4j/1.2/manual.html#defaultInit"
 * >Default Log4j initialization procedure</a>
 * @see <a href="http://logging.apache.org/log4j/1.2/manual.html#Example_Configurations"
 * >Example log4j configurations</a>
 * @see <a href="http://wiki.apache.org/logging-log4j/SystemPropertiesInConfiguration" 
 * >Log4j - how to set parameters using System Properties</a>
 */
public class LoggerInitializationListener implements ServletContextListener {
    
    /**  This property name is used to search in environment variables. */
    private static final String LOGGING_CONFIG_FILE = "POULPE_LOGGING_CONFIG_FILE";

    /** Properties file where log4j configuration file info we should check */
    private static final String PROPERTIES_FILE = "/datasource.properties";

    /** Embedded log4j configuration file path in {@code war} */
    private static final String LOG4J_EMBEDDED_CONFIGURATION_FILE = "/log4j.xml";

    /** System property witch allows to skip standard and auto Log4j initialization */
    private static final String LOG4J_INIT_OVERRIDE_PROPERTY = "log4j.defaultInitOverride";
    
    /** Prefix witch is used when this class puts messages into servlet container log stream */
    private static final String CONTAINER_LOG_PREFIX = "[POULPE][log4j init] ";
    
    /** current servlet context for logging */
    private ServletContext servletContext;
    
    /**
     * Initializing logger. For an idea how it looks configuration file, see class javadocs
     * @param event standard listener servlet event
     * @see <a href="http://wiki.apache.org/logging-log4j/Log4jXmlFormat">Log4j XML configuration apache tutorial</a>
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        
        // Save container context for logging before Log4j will be configured
        servletContext = event.getServletContext(); 
        
        // Search external Log4j configuration file
        FileInfo fileInfo = getConfigurationFileNameFromJndi();
        if (fileInfo == null) {
            fileInfo = getConfigurationFileNameFromDatasourcePropertiesFile();
        }
        if (fileInfo == null) {
            fileInfo = getConfigurationFileNameFromSystemProperties();
        }
        
        // Skip standard Log4j auto configuration on first call
        String previousLog4jInitOverrideValue = System.setProperty(LOG4J_INIT_OVERRIDE_PROPERTY, "true");
        
        // Manual Log4j configuration
        if (!loadLog4jConfigurationFromExternalFile(fileInfo)){
            loadEmbeddedLog4jConfiguration();            
        }
        
        // Return previous auto configuration property. It shared between all applications in Tomcat
        if (previousLog4jInitOverrideValue == null){
            System.clearProperty(LOG4J_INIT_OVERRIDE_PROPERTY);
        } else {
            System.setProperty(LOG4J_INIT_OVERRIDE_PROPERTY, previousLog4jInitOverrideValue);
        }

    }

    /** {@inheritDoc}  */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // Nothing to do
    }

    /**
     * Shows information about log4j configuration progress in standard servlet container log
     * @param message for logging
     */
    private void logToConsole(String message) {
        servletContext.log(CONTAINER_LOG_PREFIX+message);
    }

    /**
     * Shows information about exceptions during log4j configuration progress in standard servlet container log
     * @param message for logging
     * @param e exception
     */
    private void servletContainerlog(String message, Throwable e) {
        servletContext.log(CONTAINER_LOG_PREFIX+message, e);
    }

    /**
     * Check {@value #LOGGING_CONFIG_FILE} property from JNDI
     * @return {@link FileInfo} or {@code null}
     */
    private FileInfo getConfigurationFileNameFromJndi() {
        String logFileName = JndiAwarePropertyPlaceholderConfigurer.resolveJndiProperty(LOGGING_CONFIG_FILE);
        return logFileName == null ? null: new FileInfo("JNDI", logFileName);
    }

    /**
     * Check {@value #LOGGING_CONFIG_FILE} property in {@value #PROPERTIES_FILE} file 
     * @return {@link FileInfo} or {@code null}
     */
    private FileInfo getConfigurationFileNameFromDatasourcePropertiesFile() {
        String logFileName = loadDatasourcePropertiesFile().getProperty(LOGGING_CONFIG_FILE);
        return logFileName == null ? null: new FileInfo("\"" + PROPERTIES_FILE + "\" file", logFileName);
    }

    /**
     * Returns properties from {@value #PROPERTIES_FILE} file
     * @return {@link InputStream}
     */
    @VisibleForTesting
    protected Properties loadDatasourcePropertiesFile() {
        Resource resource = new ClassPathResource(PROPERTIES_FILE);
        try {
            return PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            servletContainerlog("Error during load \"" + PROPERTIES_FILE + "\" resource", e);
            return new Properties();
        }
    }

    /**
     * Checks {@value #LOGGING_CONFIG_FILE} property from system properties 
     * @return {@link FileInfo} or {@code null}
     */
    private FileInfo getConfigurationFileNameFromSystemProperties() {
        String logFileName = System.getProperty(LOGGING_CONFIG_FILE);
        return logFileName == null ? null: new FileInfo("system properties", logFileName);
    }
    
    /**
     * Configures log4j from external file 
     * @param fileInfo file description and path
     */
    private boolean loadLog4jConfigurationFromExternalFile(FileInfo fileInfo){
     
        if (fileInfo == null){
            return false;
        }
        logToConsole("Log4j configuration file has been taken from " + fileInfo.getSourceDescriptor() + " and set to \""
                + fileInfo.getLogFileName() + "\"");
        
        String log4jConfigurationFile = fileInfo.getLogFileName();
        File file = new File(log4jConfigurationFile.trim());
        URL url = null;
        try {
            url = file.toURI().toURL();
        } catch (MalformedURLException e) {
            return false;
        }        
        return configureLog4j(url);
    }
    
    /**
     * Configures log4j with embedded configuration file 
     */
    private void loadEmbeddedLog4jConfiguration(){
        logToConsole("Log4j embedded configuration loded");            
        URL url = getClass().getResource(LOG4J_EMBEDDED_CONFIGURATION_FILE);
        configureLog4j(url); // always returns true        
    }
    
    /**
     * Configures log4j from {@link URL} and checks regular Log4j configure class by extension (like default log4j 
     * loader)
     * @param url to configuration file
     * @return {@code true} if one or more appenders was added, {@code false} otherwise
     */
    @VisibleForTesting
    protected boolean configureLog4j(URL url){
        
        if (url.getFile().toLowerCase().endsWith(".xml")){
            DOMConfigurator.configure(url);
        } else {
            PropertyConfigurator.configure(url);
        }

        // Previous calls doesn't throw any exceptions and doesn't return fail state. 
        // So we check appenders, as most representative error indicator   
        Logger rootLogger = LogManager.getRootLogger();
        if (!rootLogger.getAllAppenders().hasMoreElements()){
            logToConsole("Log4j error during load configuraton file or no appenders presented");
            LogManager.resetConfiguration();
            return false;
        }
        return true;
    }
    
    /**
     *  Auxiliary class which contains all necessary information about file and its source 
     */
    private static class FileInfo {
        private final String sourceDescriptor;
        private final String logFileName;

        /**
         * Creates new instance file information container
         * @param sourceDescriptor user friendly descriptor about this file source 
         * @param logFileName path to file
         */
        public FileInfo(String sourceDescriptor, String logFileName) {
            this.sourceDescriptor = sourceDescriptor;
            this.logFileName = logFileName;
        }

        /**
         * @return descriptor of file
         */
        public String getSourceDescriptor() {
            return sourceDescriptor;
        }

        /**
         * @return path to file 
         */
        public String getLogFileName() {
            return logFileName;
        }
    }
}
