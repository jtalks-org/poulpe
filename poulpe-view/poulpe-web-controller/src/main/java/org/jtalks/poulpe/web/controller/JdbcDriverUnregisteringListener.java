package org.jtalks.poulpe.web.controller;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContextEvent;
import java.lang.Override;
import java.lang.String;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;


/**
 * Application shutdown listener which unregistering a JDBC driver to prevent
 * having errors while Tomcat stopping the application.
 *
 * created: 04.05.12, 12:36
 *
 * @author Leonid Kazancev
 */
public class JdbcDriverUnregisteringListener extends ContextLoaderListener {

    final private String loggerName = "org.jtalks";
    final private String successMessage = "Deregistering jdbc driver: %s";
    final private String errorMessage = "Error deregistering jdbc driver: %s";

    @Override
    public void contextInitialized(ServletContextEvent event) {
        //On application StartUP
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        //On application ShutDOWN
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                Logger.getLogger(loggerName).log(Level.INFO, String.format(successMessage, driver));
            } catch (SQLException ex) {
                Logger.getLogger(loggerName).log(Level.WARN, String.format(errorMessage, driver));
            }
        }
    }
}
