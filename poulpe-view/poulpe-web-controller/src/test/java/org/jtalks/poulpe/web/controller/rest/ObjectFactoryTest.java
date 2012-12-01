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

package org.jtalks.poulpe.web.controller.rest;

import static org.testng.Assert.assertNotNull;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Test for the {@code ObjectFactory} class.
 * 
 * @author Guram Savinov
 */
public class ObjectFactoryTest {

    private ObjectFactory factory;

    @BeforeMethod
    public void setUp() {
        factory = new ObjectFactory();
    }

    @Test
    public void createCredintals() {
        assertNotNull(factory.createCredintals());
    }

    @Test
    public void createProfile() {
        assertNotNull(factory.createProfile());
    }

    @Test
    public void createAuthentication() {
        assertNotNull(factory.createAuthentication());
    }

}
