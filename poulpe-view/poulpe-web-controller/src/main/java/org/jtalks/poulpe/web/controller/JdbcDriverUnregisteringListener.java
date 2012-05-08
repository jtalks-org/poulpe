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
    private final Logger logger = LoggerFactory.getLogger(getClass());

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
        deregisterDrivers(getDrivers());
    }

    Enumeration<Driver> getDrivers() {
        return DriverManager.getDrivers();
    }

    void deregisterDrivers(Enumeration<Driver> drivers) {
        while (drivers.hasMoreElements()) {
            deregisterDriver(drivers.nextElement());
        }
    }

    void deregisterDriver(Driver driver) {
        try {
            DriverManager.deregisterDriver(driver);
            logger.info("Deregistering JDBC driver: {}", driver);
        } catch (SQLException e) {
            logger.warn("Error deregistering JDBC driver: {}. Root cause: ", driver, e);
        }
    }
}



