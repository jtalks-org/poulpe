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

import com.google.common.annotations.VisibleForTesting;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;
import org.jtalks.poulpe.model.utils.JndiAwarePropertyPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;


/**
 * Application startup listener which initializes log4j from external or embedded configuration file.
 * <ol>
 * <li>It allows configuring our environments without unpacking and changing log4j inside of war file </li>
 * <li>It allows changing logs location as well as configuration depending on environment</li>
 * <li>It gives an ability e.g. on PROD to disable default {@code DEBUG} level turned on</li>
 * </ol>
 * <p>This listener should be registered and started before any other servlet/listener (which use logging). So it
 * should start before usual logger initialization.</p>
 * <p>Listener searches configuration file in common order:
 * <ol>
 * <li>JNDI</li>
 * <li>datasource.properties file</li>
 * <li>system properties</li>
 * <li>embedded</li>
 * </ol></p>
 * <p>We can't use Spring IoC, because order of bean initialization is not managed and some classes can start using
 * logger which is configured differently.</p>
 *
 * @author Evgeny Kapinos
 * @see <a href="http://logging.apache.org/log4j/1.2/manual.html#defaultInit"
 *      >Default Log4j initialization procedure</a>
 * @see <a href="http://logging.apache.org/log4j/1.2/manual.html#Example_Configurations"
 *      >Sample log4j configuration</a>
 * @see <a href="http://wiki.apache.org/logging-log4j/SystemPropertiesInConfiguration"
 *      >Log4j - how to set parameters using System Properties</a>
 */
public class LoggerInitializationListener implements ServletContextListener {
    /** This property name is used to search in environment variables. */
    private static final String LOGGING_CONFIG_FILE_PROP_NAME = "POULPE_LOGGING_CONFIG_FILE";
    /** Properties file where log4j configuration file info we should check (inside classpath) */
    private static final String PROPERTIES_FILE = "/datasource.properties";
    /** Embedded log4j configuration file path in classpath */
    private static final String LOG4J_EMBEDDED_CONFIGURATION_FILE = "/log4j.xml";
    /** System property witch allows to skip standard and auto Log4j initialization */
    private static final String LOG4J_INIT_OVERRIDE_PROPERTY = "log4j.defaultInitOverride";
    /** Prefix witch is used when this class puts messages into servlet container log stream (Tomcat's catalina.out) */
    private static final String CONTAINER_LOG_PREFIX = "[POULPE][log4j init] ";
    /** current servlet context for logging */
    private ServletContext servletContext;

    /**
     * Initializing logger. For an idea how configuration file looks like, see class javadocs.
     *
     * @param event standard listener servlet event
     * @see <a href="http://wiki.apache.org/logging-log4j/Log4jXmlFormat">Log4j XML configuration apache tutorial</a>
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        servletContext = event.getServletContext();
        FileInfo fileInfo = log4jConfigLocation();
        // Skip standard Log4j auto configuration on first call
        String previousLog4jInitOverrideValue = System.setProperty(LOG4J_INIT_OVERRIDE_PROPERTY, "true");
        if (!loadLog4jConfigurationFromExternalFile(fileInfo)) {
            loadEmbeddedLog4jConfiguration();
        }
        // Return previous auto configuration property. It's shared between all applications in Tomcat
        if (previousLog4jInitOverrideValue == null) {
            System.clearProperty(LOG4J_INIT_OVERRIDE_PROPERTY);
        } else {
            System.setProperty(LOG4J_INIT_OVERRIDE_PROPERTY, previousLog4jInitOverrideValue);
        }
    }

    /** Looks into JNDI, properties file and env vars for the location of logger config. */
    private FileInfo log4jConfigLocation() {
        FileInfo fileInfo = getConfigurationFileNameFromJndi();
        if (fileInfo == null) {
            fileInfo = getConfigurationFileNameFromDatasourcePropertiesFile();
        }
        if (fileInfo == null) {
            fileInfo = getConfigurationFileNameFromSystemProperties();
        }
        return fileInfo;
    }

    /** {@inheritDoc} */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // Nothing to do
    }

    /**
     * Since log4j is not yet configured, logging everything into standard output of the app server
     * (catalina.out in Tomcat).
     *
     * @param message string for logging
     */
    private void logToConsole(String message) {
        servletContext.log(CONTAINER_LOG_PREFIX + message);
    }

    /** Same as {@link #logToConsole(String)}, but also logs exceptions. */
    private void logToConsole(String message, Throwable e) {
        servletContext.log(CONTAINER_LOG_PREFIX + message, e);
    }

    /**
     * Gets {@value #LOGGING_CONFIG_FILE_PROP_NAME} property from JNDI.
     *
     * @return {@link FileInfo} or {@code null}
     */
    private FileInfo getConfigurationFileNameFromJndi() {
        String logFileName = JndiAwarePropertyPlaceholderConfigurer.resolveJndiProperty(LOGGING_CONFIG_FILE_PROP_NAME);
        return logFileName == null ? null : new FileInfo(FileInfo.Source.JNDI, logFileName);
    }

    /**
     * Gets {@value #LOGGING_CONFIG_FILE_PROP_NAME} property in {@value #PROPERTIES_FILE} file.
     *
     * @return {@link FileInfo} or {@code null}
     */
    private FileInfo getConfigurationFileNameFromDatasourcePropertiesFile() {
        String logFileName = loadDatasourcePropertiesFile().getProperty(LOGGING_CONFIG_FILE_PROP_NAME);
        return logFileName == null ? null : new FileInfo(FileInfo.Source.PROPERTIES_FILE, logFileName);
    }

    /** Returns properties from {@value #PROPERTIES_FILE} file. */
    @VisibleForTesting
    protected Properties loadDatasourcePropertiesFile() {
        Resource resource = new ClassPathResource(PROPERTIES_FILE);
        try {
            return PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            logToConsole("Error during load \"" + PROPERTIES_FILE + "\" resource", e);
            return new Properties();
        }
    }

    /**
     * Gets {@value #LOGGING_CONFIG_FILE_PROP_NAME} property from system properties.
     *
     * @return {@link FileInfo} or {@code null}
     */
    private FileInfo getConfigurationFileNameFromSystemProperties() {
        String logFileName = System.getProperty(LOGGING_CONFIG_FILE_PROP_NAME);
        return logFileName == null ? null : new FileInfo(FileInfo.Source.PROPERTIES_FILE, logFileName);
    }

    /**
     * Configures log4j from external file
     *
     * @param fileInfo file description and path
     */
    private boolean loadLog4jConfigurationFromExternalFile(FileInfo fileInfo) {
        if (fileInfo == null) {
            return false;
        }
        logToConsole("Log4j configuration file has been taken from " + fileInfo.getSource() + " and set to ["
                + fileInfo.getLogConfigPath() + "]");
        String log4jConfigurationFile = fileInfo.getLogConfigPath();
        File file = new File(log4jConfigurationFile.trim());
        URL url;
        try {
            url = file.toURI().toURL();
        } catch (MalformedURLException e) {
            return false;
        }
        return configureLog4j(url);
    }

    /** Configures log4j with embedded configuration file */
    private void loadEmbeddedLog4jConfiguration() {
        logToConsole("Log4j embedded configuration loaded");
        URL url = getClass().getResource(LOG4J_EMBEDDED_CONFIGURATION_FILE);
        configureLog4j(url); // always returns true        
    }

    /**
     * Configures log4j from {@link URL} and checks regular Log4j configure class by extension (like default log4j
     * loader)
     *
     * @param url to configuration file
     * @return {@code true} if one or more appenders was added, {@code false} otherwise
     */
    @VisibleForTesting
    protected boolean configureLog4j(URL url) {
        if (url.getFile().toLowerCase().endsWith(".xml")) {
            DOMConfigurator.configure(url);
        } else {
            PropertyConfigurator.configure(url);
        }

        // Previous calls doesn't throw any exceptions and doesn't return fail state. 
        // So we check appenders, as most representative error indicator   
        Logger rootLogger = LogManager.getRootLogger();
        if (!rootLogger.getAllAppenders().hasMoreElements()) {
            logToConsole("Log4j error during load configuration file or no appenders presented");
            LogManager.resetConfiguration();
            return false;
        }
        return true;
    }

    /** Auxiliary class which contains all necessary information about file and its source */
    private static class FileInfo {
        private enum Source {PROPERTIES_FILE, SYSTEM_PROPERTIES, JNDI}

        private final String logConfigPath;
        private final Source source;

        public FileInfo(Source source, String logConfigPath) {
            this.source = source;
            this.logConfigPath = logConfigPath;
        }

        /** @return properties? env vars? jndi? */
        public String getSource() {
            return source.toString();
        }

        /** @return path to file */
        public String getLogConfigPath() {
            return logConfigPath;
        }
    }
}
