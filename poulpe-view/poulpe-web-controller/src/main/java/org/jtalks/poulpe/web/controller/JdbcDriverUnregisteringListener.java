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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;


/**
 * Application shutdown listener which unregistering a JDBC driver to prevent having errors while Tomcat stopping the
 * application.
 *
 * @author Leonid Kazancev
 * @see <a href="https://issues.apache.org/jira/browse/DBCP-332">Apache.org</a>
 */
public class JdbcDriverUnregisteringListener implements ServletContextListener {

    /** 
     * Instance of this class created like any other {@link ServletContextListener} on application startup.
     * We can manage order of call {@link #contextInitialized(ServletContextEvent)} and 
     * {@link #contextDestroyed(ServletContextEvent)} via {@code web.xml}. But servlet container (like Tomcat) creates
     * all instances of listeners together. 
     * 
     * <p>So, if we initialize logger field ordinary (by class field in listener),
     * then it will be initialized before first {@link #contextInitialized(ServletContextEvent)} and we lose
     * opportunity to initialize system properties (like {@code LOG_FILE}) via 
     * {@link org.jtalks.poulpe.web.controller.LoggerInitializationListener LoggerInitializationListener}</p>
     * 
     * <p><b>Best practices is</b>: Do not directly initialize fields of servlet listeners. Use 
     * {@link #contextInitialized(ServletContextEvent)} and {@link #contextDestroyed(ServletContextEvent)} methods</p> 
     */
    private Logger logger;

    /**
     * {@inheritDoc}
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        logger = LoggerFactory.getLogger(getClass());
        deregisterDrivers(getDrivers());
    }

    /**
     * Retrieves an Enumeration with all of the currently loaded JDBC drivers.
     *
     * @return the list of JDBC Drivers
     */
    Enumeration<Driver> getDrivers() {
        return DriverManager.getDrivers();
    }

    /**
     * Unregistering JDBC drivers given as param.
     *
     * @param drivers {@link Enumeration} of {@link Driver} to unregister
     */
    void deregisterDrivers(Enumeration<Driver> drivers) {
        while (drivers.hasMoreElements()) {
            deregisterDriver(drivers.nextElement());
        }
    }

    /**
     * Unregistering single JDBC driver given as param.
     *
     * @param driver to unregister
     */
    void deregisterDriver(Driver driver) {
        try {
            DriverManager.deregisterDriver(driver);
            logger.info("Deregistering JDBC driver: {}", driver);
        } catch (SQLException e) {
            logger.warn("Error deregistering JDBC driver: {}. Root cause: ", driver, e);
        }
    }
}



