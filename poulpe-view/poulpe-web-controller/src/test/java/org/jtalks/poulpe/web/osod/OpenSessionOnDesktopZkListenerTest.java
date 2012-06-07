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
package org.jtalks.poulpe.web.osod;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Execution;

import static org.mockito.Mockito.*;

/**
 * @author stanislav bashkirtsev
 */
public class OpenSessionOnDesktopZkListenerTest {
    OpenSessions openSessions;
    OpenSessionOnDesktopZkListener sut;

    @BeforeMethod
    public void setUp() throws Exception {
        openSessions = mock(OpenSessions.class);
        sut = new OpenSessionOnDesktopZkListener(openSessions);
    }

    @Test(dataProvider = "provideExecutionWithDesktopInside")
    public void testCleanup(Execution execution) throws Exception {
        sut.cleanup(execution, null, null);
        verify(openSessions).unbindSession();
    }

    @Test(dataProvider = "provideExecutionWithDesktopInside", expectedExceptions = IllegalArgumentException.class)
    public void cleanUpShouldRethrowExceptionIfItHappens(Execution execution) throws Exception {
        doThrow(new IllegalArgumentException()).when(openSessions).unbindSession();
        sut.cleanup(execution, null, null);
    }

    @Test(dataProvider = "provideExecutionWithDesktopInside")
    public void testInit(Execution execution) throws Exception {
        sut.init(execution, null);
        verify(openSessions).openSession(execution.getDesktop().getId());
    }

    @Test(dataProvider = "provideExecutionWithDesktopInside", expectedExceptions = IllegalArgumentException.class)
    public void cleanUpShouldRethrowSameException(Execution execution) throws Exception {
        doThrow(new IllegalArgumentException()).when(openSessions).openSession(anyString());
        sut.init(execution, null);
    }

    @Test(dataProvider = "provideDesktop")
    public void testCleanupDesktop(Desktop desktop) throws Exception {
        sut.cleanup(desktop);
        verify(openSessions).closeSession(desktop.getId());
    }

    @Test(dataProvider = "provideDesktop", expectedExceptions = IllegalArgumentException.class)
    public void cleanupDesktopShouldRethrowSameException(Desktop desktop) throws Exception {
        doThrow(new IllegalArgumentException()).when(openSessions).closeSession(anyString());
        sut.cleanup(desktop);
    }

    @DataProvider
    public Object[][] provideDesktop() {
        Desktop desktop = mock(Desktop.class);
        doReturn("test-id").when(desktop).getId();
        return new Object[][]{{desktop}};
    }

    @DataProvider
    public Object[][] provideExecutionWithDesktopInside() {
        Execution execution = mock(Execution.class);
        Desktop desktop = mock(Desktop.class);
        doReturn("test-id").when(desktop).getId();
        doReturn(desktop).when(execution).getDesktop();
        return new Object[][]{{execution}};
    }
}
