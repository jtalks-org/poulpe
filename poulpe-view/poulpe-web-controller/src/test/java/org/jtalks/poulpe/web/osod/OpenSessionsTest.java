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
