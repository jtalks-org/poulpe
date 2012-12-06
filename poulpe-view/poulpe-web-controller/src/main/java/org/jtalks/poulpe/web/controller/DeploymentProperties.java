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

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;

/**
 * Auxiliary class for viewing server info. Created automatically by spring IoC (singleton scope). It provides the most
 * important information about the last deployment like its time, database, server IP, etc.
 *
 * @author Evgeny Kapinos
 */
public class DeploymentProperties {
    
    String deploymentDate;
    String databaseName;
    String databaseUser;
    String serverIP;
    
    
    /** 
     * Constructor for initialization variables
     */
    DeploymentProperties(DataSource dataSource){
        
        this.deploymentDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date());
        
        try {
            Connection connection = dataSource.getConnection();
            databaseName = connection.getCatalog();
            DatabaseMetaData connectionMetadata = connection.getMetaData();
            databaseUser = connectionMetadata.getUserName();
            connection.close();
        } catch (SQLException e) {
            databaseName = null;
            databaseUser = null;
        }
         
        try {
            this.serverIP = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            this.serverIP = null;
        }
    }

    /**
     * Returns formatted deployment date for view 
     *
     * @return deployment date 
     */
    public String getDeploymentDate() {
        return deploymentDate;
    }    
    
    /**
     * Returns database name for view 
     *
     * @return database name 
     */
    public String getDatabaseName() {
        return databaseName;
    }

    /**
     * Returns database user for view 
     *
     * @return database user
     */
    public String getDatabaseUser() {
        return databaseUser;
    }

    /**
     * Returns current web server IP 
     *
     * @return web server IP
     */
    public String getServerIP() {
        return serverIP;
    }

}
