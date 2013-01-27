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
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.jtalks.poulpe.model.utils.JndiAwarePropertyPlaceholderConfigurer;

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
     * ambiguity we should use unique and seldom name. All JTalks modules should use separated logs too.
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
        
        String sourceDescriptor = null;
        String logFile = JndiAwarePropertyPlaceholderConfigurer.resolveJndiProperty(LOG_FILE_USER_PROPERTY);
        
        if (logFile != null) {
            sourceDescriptor = "JNDI";
        } else {
            Properties prop = new Properties();
            try {
                prop.load(getClass().getResourceAsStream(PROPERTIES_FILE));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            logFile = prop.getProperty(LOG_FILE_USER_PROPERTY);

            if (logFile != null) {
                sourceDescriptor = "\""+PROPERTIES_FILE+"\" file";
            } else {
                logFile = System.getProperty(LOG_FILE_USER_PROPERTY);

                if (logFile != null) {
                    sourceDescriptor = "system properties";
                } else {
                    logFile = LOG_FILE_VALUE_DEFAULT;
                    sourceDescriptor = "default value";
                }
            }
        }
        // Console output. It's not mistake. No sense to put information about log file path in the same log file 
        System.out.println("Log file path taken from "+sourceDescriptor+" and set to \""+logFile+"\"");
        
        System.setProperty(LOG_FILE_SYSTEM_PROPERTY, logFile);             
    }

    /**
     * Clean JVM system properties which was used like placeholders.
     * <p>{@inheritDoc}</p>
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        System.clearProperty(LOG_FILE_SYSTEM_PROPERTY);
    }
}
