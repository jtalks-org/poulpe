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
package org.jtalks.poulpe.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.jtalks.poulpe.model.utils.JndiAwarePropertyPlaceholderConfigurer;

import com.google.common.annotations.VisibleForTesting;

/**
 * Application startup listener which initialize logger properties which used during standard logger initialization 
 * (reading {@code log4j.xml}). 
 * 
 * <p>This listener should be registered and started before any servlet/listener (which used logging)
 * in {@code web.xml}. So it will start before standard logger initialization</p>
 *
 * <p>Logger search for parameters in common order:
 *  <ol>
 *  <li>JNDI</li>
 *  <li>datasource.properties file</li>
 *  <li>system properties</li>
 *  <li>default value</li>
 *  </ol></p>
 * 
 * <p>We can't use Spring IoC, because order of bean initialization unmanaged</p>
 * 
 * @author Evgeny Kapinos
 * @see <a href="http://logging.apache.org/log4j/1.2/manual.html">Log4j - manual</a>,
 *      "<b>Initialization servlet</b>" paragraph
 * @see <a href="http://wiki.apache.org/logging-log4j/SystemPropertiesInConfiguration"
 *      >Log4j - how to set parameters using System Properties</a>
 */
public class LoggerInitializationListener implements ServletContextListener {
    
    /**  This property name used for search in environment */
    private static final String LOG_FILE_USER_PROPERTY = "LOG_FILE";
    
    /**  
     * This property name used as real placeholder in logger configuration file  
     * For usage placeholder in log4j.xml we should set system properties. But system properties have JVM
     * scope. After first setting, we share these properties for all applications until server restart. To prevent 
     * ambiguity we should use unique and seldom name. All JTalks modules should use separated placeholders too.
     * 
     * So we expect {@value #LOG_FILE_USER_PROPERTY} property form user and use it value to set 
     * real placeholder {@value #LOG_FILE_SYSTEM_PROPERTY}    
     */
    private static final String LOG_FILE_SYSTEM_PROPERTY = "LOG_FILE_POULPE";
    
    /** Properties file */
    private static final String PROPERTIES_FILE = "/datasource.properties";

    /** Default log file path */
    private static final String LOG_FILE_VALUE_DEFAULT = "../logs/poulpe.log";
    //private static final String LOG_FILE_VALUE_DEFAULT = "${catalina.base}/logs/poulpe.log";

    /**
     * Initializing placeholders for logger configuration file
     * <p>{@inheritDoc}</p>
     */
    @Override
    public void contextInitialized(ServletContextEvent event){
        
        logFileInfo logFileInfo = getLogFileNameFromJNDI();       
        
        if (logFileInfo == null) {
            logFileInfo = getLogFileNameFromDatasourcePropertiesFile();
        }
        
        if (logFileInfo == null) {
            logFileInfo = getLogFileNameFromDatasourcePropertiesFile();
        } 
        
        if (logFileInfo == null) {
            logFileInfo = getLogFileNameFromSystemProperties();
        }
       
        if (logFileInfo == null) {
            logFileInfo = getDefaultLogFileName();
        }
         
        logToConsole("Log file name taken from "+logFileInfo.getSourceDescriptor()+" and set to \""
                     + logFileInfo.getLogFileName()+"\"");
        
        System.setProperty(LOG_FILE_SYSTEM_PROPERTY, logFileInfo.getLogFileName());             
    }

    /**
     * Clean JVM system properties which was used like placeholders.
     * <p>{@inheritDoc}</p>
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        System.clearProperty(LOG_FILE_SYSTEM_PROPERTY);
    }
    
    /**
     * Console output. It's not mistake. No sense to put information about:
     * <ol>
     * <li>logger initialization fails without log file</li> 
     * <li>log file destination in same log</li>
     * </ol>
     * 
     * <p>By default Tomcat redirects console (out and err streams) to <b>catalina.out</b> file</p>
     * 
     * @see <a href="http://wiki.apache.org/tomcat/FAQ/Logging#Q6">About standard streams at Tomcat FAQ</a>  
     * @param message for logging
     */
    private void logToConsole(String message){
        System.out.println(message); //NOSONAR
    }
    
    /**
     * Check {@value #LOG_FILE_USER_PROPERTY} property from JNDI
     * @return {@link logFileInfo} or {@code null}
     */
    private logFileInfo getLogFileNameFromJNDI(){      
        String logFileName = JndiAwarePropertyPlaceholderConfigurer.resolveJndiProperty(LOG_FILE_USER_PROPERTY);      
        if (logFileName == null) {
            return null;
        }
        return new logFileInfo("JNDI", logFileName);
    }
    
    /**
     * Check {@value #LOG_FILE_USER_PROPERTY} property from {@value #PROPERTIES_FILE} file 
     * @return {@link logFileInfo} or {@code null}
     */
    private logFileInfo getLogFileNameFromDatasourcePropertiesFile(){        
        Properties prop = new Properties();
        InputStream propertiesFileStream = null;
        String logFileName = null;
        try {
            propertiesFileStream = getPropertiesFileStream();
            prop.load(propertiesFileStream);
            logFileName = prop.getProperty(LOG_FILE_USER_PROPERTY);
        } catch (IOException e) {
            logToConsole("Error during reading \""+PROPERTIES_FILE+"\" stream: "+e.toString());                   
        } finally {
            if (propertiesFileStream != null) {
                try {
                    propertiesFileStream.close();
                } catch (IOException e) {
                    logToConsole("Error during closing \""+PROPERTIES_FILE+"\" stream: "+e.toString());
                }
            }
        }        
        if (logFileName == null) {
            return null;
        }       
        return new logFileInfo("\""+PROPERTIES_FILE+"\" file", logFileName);
    }
    
    /**
     * Opens file with properties and return stream
     * @return {@link InputStream}
     */
    @VisibleForTesting
    protected InputStream getPropertiesFileStream(){
        return getClass().getResourceAsStream(PROPERTIES_FILE);
    }

    /**
     * Check {@value #LOG_FILE_USER_PROPERTY} property from system properties 
     * @return {@link logFileInfo} or {@code null}
     */
    private logFileInfo getLogFileNameFromSystemProperties(){
        String logFileName = System.getProperty(LOG_FILE_USER_PROPERTY);        
        if (logFileName == null) {
            return null;
        }        
        return new logFileInfo("system properties", logFileName);       
    }
    
    /**
     * @return {@link logFileInfo} with default value {@value #LOG_FILE_VALUE_DEFAULT}
     */
    private logFileInfo getDefaultLogFileName(){       
        return new logFileInfo("default value", LOG_FILE_VALUE_DEFAULT);       
    }

    /**
     *  Auxiliary class which contains all necessary information about log file and its source 
     */
    private static class logFileInfo {
        private final String sourceDescriptor;
        private final String logFileName;

        /**
         * Creates new instance log file information container
         * @param sourceDescriptor user friendly descriptor about its log file source 
         * @param logFileName path to log file
         */
        public logFileInfo(String sourceDescriptor, String logFileName) {
            this.sourceDescriptor = sourceDescriptor;
            this.logFileName = logFileName;
        }

        /**
         * @return descriptor of log file
         */
        public String getSourceDescriptor() {
            return sourceDescriptor;
        }

        /**
         * @return path to log file 
         */
        public String getLogFileName() {
            return logFileName;
        }      
    }
}
