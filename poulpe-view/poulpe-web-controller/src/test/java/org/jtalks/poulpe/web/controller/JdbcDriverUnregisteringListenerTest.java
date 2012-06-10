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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.sql.Driver;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

import javax.servlet.ServletContextEvent;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author @author Leonid Kazancev
 */
public class JdbcDriverUnregisteringListenerTest {
    JdbcDriverUnregisteringListener spyJdbcDriverUnregisteringListener = spy(new JdbcDriverUnregisteringListener());
    Driver driver;
    Enumeration<Driver> drivers;

    @BeforeMethod
    public void setUp() throws Exception {
        driver = mock(Driver.class);
        drivers = Collections.enumeration(Arrays.asList(driver));
    }

    @Test
    public void testContextDestroyed() {
        when(spyJdbcDriverUnregisteringListener.getDrivers()).thenReturn(drivers);
        doNothing().when(spyJdbcDriverUnregisteringListener).deregisterDriver(driver);

        spyJdbcDriverUnregisteringListener.contextDestroyed(any(ServletContextEvent.class));
        verify(spyJdbcDriverUnregisteringListener).deregisterDriver(driver);
    }


}
