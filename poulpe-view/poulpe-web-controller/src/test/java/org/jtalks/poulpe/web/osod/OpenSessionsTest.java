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

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertSame;

/**
 * @author stanislav bashkirtsev
 */
public class OpenSessionsTest {
    OpenSessions sut;

    @BeforeMethod
    public void setUp() throws Exception {
        sut = new OpenSessions(mock(SessionFactory.class));
    }

    @Test
    public void openSessionShouldDoNothingIfAlreadyBound() throws Exception {
        OpenSessions sut = givenSpyOfSut(this.sut);
        doReturn(false).when(sut).noSessionBoundToThread();
        sut.openSession("test-desktop");
    }

    @Test
    public void openSessionShouldBindToThread() throws Exception {
        OpenSessions sut = givenSpyOfSut(this.sut);
        Session session = mock(Session.class);
        doReturn(true).when(sut).noSessionBoundToThread();
        doReturn(session).when(sut).getOrCreateSession("test-desktop");
        doNothing().when(sut).bindToThread(session);

        sut.openSession("test-desktop");
        verify(sut).bindToThread(session);
    }

    @Test
    public void getOrCreateSessionShouldGet() throws Exception {
        Session session = givenSutWithSession(sut, "desktop-id");
        org.hibernate.Session resultSession = sut.getOrCreateSession("desktop-id");
        assertSame(resultSession, session);
    }

    @Test
    public void getOrCreateSessionShouldCreate() throws Exception {
        OpenSessions sut = givenSpyOfSut(this.sut);
        doReturn(mock(Session.class)).when(sut).createSession();

        Session resultSession = sut.getOrCreateSession("desktop-id");
        assertSame(resultSession, resultSession);
    }

    @Test
    public void closeSessionThatNonExistsShouldDoNothing() throws Exception {
        sut.closeSession("non-existing-id");
    }

    @Test
    public void closeSession() throws Exception {
        Session session = givenSutWithSession(sut, "desktop-id");
        sut.closeSession("desktop-id");
        verify(session).close();
    }

    private Session givenSutWithSession(OpenSessions sut, String desktopId) throws Exception {
        Session session = mock(Session.class);
        Field sessionsField = sut.getClass().getDeclaredField("sessions");
        sessionsField.setAccessible(true);
        ((Map<String, Session>) sessionsField.get(sut)).put(desktopId, session);
        return session;
    }

    private OpenSessions givenSpyOfSut(OpenSessions sut) {
        return spy(sut);
    }
}
