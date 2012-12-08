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
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.sql.DataSource;

/**
 * Auxiliary class for viewing server info. Created automatically by spring IoC (singleton scope). It provides the most
 * important information about the last deployment like its time, database, server IP, etc.
 *
 * @author Evgeny Kapinos
 */
public class DeploymentProperties {
    
    private DataSource dataSource;
    private String deploymentDate;
    private String databaseServer;
    private String databaseName;
    private String databaseUser;
    private String serverIP;  
    
    /** 
     * Constructor for initialization variables
     */
    DeploymentProperties(DataSource dataSource){
        
        this.dataSource = dataSource;
 
    }
    
    /** 
     * Initialization of calculated variables 
     * 
     */
    public void init(){
        
        this.deploymentDate = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date());
        
        DatabaseInfo databaseInfo = databaseInfo();
        this.databaseServer = databaseInfo.getDatabaseServer();
        this.databaseName   = databaseInfo.getDatabaseName();
        this.databaseUser   = databaseInfo.getDatabaseUser();
             
        this.serverIP = serverIP();
    }
    
    /** 
     * Method collect information about database connection 
     * 
     * @return Nested class with server info
     */
    private DatabaseInfo databaseInfo(){ 
        
        String databaseName   = null;
        String databaseUser   = null;
        String databaseServer = null;
        
        try {
            
            Connection connection = dataSource.getConnection();
            databaseName = connection.getCatalog();
            DatabaseMetaData connectionMetadata = connection.getMetaData();
            databaseUser = connectionMetadata.getUserName();
                      
            String url = connectionMetadata.getURL();
            String cleanURI = url.substring(5); // subtarct "jdbc:"
            URI uri = URI.create(cleanURI);
            databaseServer = (uri.getHost() != null ? uri.getHost() : "N/A")  + (uri.getPort() == -1 ? "" : String.valueOf(uri.getPort()));
            
            connection.close();
            
        } catch (SQLException e) {
            // No info data
        }
        
        return new DatabaseInfo(databaseServer, databaseName, databaseUser);
    }
    
    /** 
     * Method collect information about used server IP. If sever has
     * global Internet addresses, they return. If no global dresses, 
     * method returns all found Intranet addresses. If sever hasn't
     * any IP loopback returns.    
     * 
     * @return web server IP
     */
    private String serverIP(){
        
        StringBuilder globalAddressesSB = new StringBuilder();
        StringBuilder localAddressesSB = new StringBuilder();
        
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {               
                
                NetworkInterface networkInterface = en.nextElement();

                // Also we should skip loopback address, if we didn't find anything better.
                if (!networkInterface.isUp() || networkInterface.isVirtual() || networkInterface.isLoopback()) {
                    continue;
                }

                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {

                    InetAddress inetAddress = interfaceAddress.getAddress();
                    if (inetAddress.isLinkLocalAddress()) {
                        continue;
                    }

                    if (!inetAddress.isSiteLocalAddress()) {
                        if (globalAddressesSB.length() > 0) {
                            globalAddressesSB.append(", ");
                        }
                        globalAddressesSB.append(inetAddress.getHostAddress());
                    } else {
                        if (localAddressesSB.length() > 0) {
                            localAddressesSB.append(", ");
                        }
                        localAddressesSB.append(inetAddress.getHostAddress());
                    }
                }
            }
            if(globalAddressesSB.length() > 0){
                return globalAddressesSB.toString();
            } else if (localAddressesSB.length() > 0){
                return localAddressesSB.toString();
            } else {
                // Nothing found. So we'll show default (loopback)
                return InetAddress.getLocalHost().getHostAddress();
            }
        } catch (SocketException e) {
            // No info data
        } catch (UnknownHostException e) {
            // No info data
        }
        
        return null;
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
     * Returns database server for view
     *
     * @return database server 
     */
    public String getDatabaseServer() {
        return databaseServer;
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
    
    /**
     * Auxiliary nested class, which contain all necessary info about database    
     *
     */
    private class DatabaseInfo {
        private String databaseServer;
        private String databaseName;
        private String databaseUser;

        public DatabaseInfo(String databaseServer, String databaseName, String databaseUser) {
            this.databaseServer = databaseServer;
            this.databaseName = databaseName;
            this.databaseUser = databaseUser;
        }

        public String getDatabaseServer() {
            return databaseServer;
        }

        public String getDatabaseName() {
            return databaseName;
        }
        
        public String getDatabaseUser() {
            return databaseUser;
        }
    }

}
