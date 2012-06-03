package org.jtalks.poulpe.web.osod;

import com.google.common.annotations.VisibleForTesting;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A class that controls the Hibernate Sessions - starts, finishes, clears them, etc. It's responsible for binding
 * sessions to current thread so that Spring Transaction Manager can find the already opened session and use it to work
 * with hibernate. This class is usually used by {@link OpenSessionOnDesktopZkListener} out of box, but if you want to
 * manually influence the Hibernate Session (e.g. you want  to clear it because of memory leaks), you can work with this
 * object directly.
 *
 * @author stanislav bashkirtsev
 * @see OpenSessionOnDesktopZkListener
 */
public class OpenSessions {
    private final ConcurrentMap<String, Session> sessions = new ConcurrentHashMap<String, Session>();
    private final SessionFactory sessionFactory;

    public OpenSessions(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void openSession(String desktopId) {
        if (noSessionBoundToThread()) {
            Session session = getOrCreateSession(desktopId);
            sessions.putIfAbsent(desktopId, session);
            bindToThread(session);
        }
    }

    public void unbindSession() {
        TransactionSynchronizationManager.unbindResource(sessionFactory);
    }

    public void closeSession(String desktopId) {
        Session session = sessions.remove(desktopId);
        if (session != null) {
            session.close();
        }
    }

    @VisibleForTesting
    Session getOrCreateSession(String desktopId) {
        Session session = sessions.get(desktopId);
        if (session == null) {
            session = createSession();
            session.setFlushMode(FlushMode.MANUAL);
        }
        return session;
    }

    @VisibleForTesting
    Session createSession() {
        return SessionFactoryUtils.getSession(sessionFactory, true);
    }

    @VisibleForTesting
    void bindToThread(Session session) {
        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
    }

    @VisibleForTesting
    boolean noSessionBoundToThread() {
        return !TransactionSynchronizationManager.hasResource(sessionFactory);
    }
}
