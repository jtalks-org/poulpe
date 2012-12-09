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
package org.jtalks.poulpe.web.controller;

import static org.testng.Assert.*;

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
public class DeploymentPropertiesTest {
    private DeploymentProperties deploymentProperties; 
    private EmbeddedDatabase dataSource;

    /**
     * The method setups an in-memory database and initialize empty db schema.
     */
    @BeforeClass
    private void setUp() {
        dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL).build();
        deploymentProperties = new DeploymentProperties(dataSource);
    }

    /**
     * The method run initialization method and check results.
     */
    @Test
    public void initTest() {       
         deploymentProperties.init();       
         assertNotNull(deploymentProperties.getDeploymentDate());
         assertNotNull(deploymentProperties.getDatabaseServer());
         assertEquals(deploymentProperties.getDatabaseUser(), "SA");
         assertNotNull(deploymentProperties.getDatabaseName(), "PUBLIC");
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
