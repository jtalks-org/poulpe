/**
 * Copyright (C) 2012  JTalks.org Team
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
package org.jtalks.poulpe.service;

import static org.testng.Assert.*;

import org.jtalks.poulpe.service.DeploymentProperties;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests that DeploymentProperties correctly initialized in startup, uses embedded database to make things more real.
 * It's actually a bit more than unit test because it uses real network and database resources.
 * 
 * @author Evgeny Kapinos
 * 
 */
public class DeploymentPropertiesServiceTest {
    private DeploymentProperties deploymentProperties; 
    private EmbeddedDatabase dataSource;

    /**
     * The method setups an in-memory database and initialize empty database schema.
     */
    @BeforeClass
    private void setUp() {
        dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL).setName("PUBLIC").build();
        deploymentProperties = new DeploymentProperties(dataSource);
    }

    /**
     * The method run initialization method and check results.
     */
    @Test(enabled=false)
    public void initTest() {       
        
        deploymentProperties.init();
           
        // If initialization completed successfully, deployment date will be filled with some String.   
        assertNotNull(deploymentProperties.getDeploymentDate());
        
        // In-memory DB always hasn't info about server host name. So we got "N/A"
        assertEquals(deploymentProperties.getDatabaseServer(), "N/A"); 
        assertEquals(deploymentProperties.getDatabaseUser(),   "SA");
        assertEquals(deploymentProperties.getDatabaseName(),   "PUBLIC");
        
        // If initialization completed successfully, server IP will be filled with some String 
        // In worse case it will "127.0.0.1", but not empty    
        assertNotNull(deploymentProperties.getServerIP());
    }
        
    /**
     * Closes previously opened resources such as database connection.
     */
    @AfterClass
    public void tearDown() {
       dataSource.shutdown();
    }

 }
