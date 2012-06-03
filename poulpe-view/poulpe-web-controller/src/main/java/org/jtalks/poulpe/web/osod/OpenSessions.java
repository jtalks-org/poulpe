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

    /**
     * Opens a new session and binds it to current thread. Results in no-op if there is already a session bound. Binding
     * to the tread means saving the session as a {@link ThreadLocal} variable for the thread so that any class at any
     * point can access that variable (particularly Spring Transaction related classes) within a single thread, it won't
     * be visible to other threads.
     *
     * @param desktopId a ZK desktop ID to associate session with, later it will be possible to close session having
     *                  this id
     */
    public void openSession(String desktopId) {
        if (noSessionBoundToThread()) {
            Session session = getOrCreateSession(desktopId);
            sessions.putIfAbsent(desktopId, session);
            bindToThread(session);
        }
    }

    /**
     * Unbinds (removes from the {@link ThreadLocal} variables of the thread) the session which means that Spring
     * Transaction related classes won't have access to the session anymore. This usually should be done when request
     * processing is finished. This doesn't close the session, it still there and is associated with desktop id.
     */
    public void unbindSession() {
        TransactionSynchronizationManager.unbindResource(sessionFactory);
    }

    /**
     * Closes the session inside the {@link OpenSessions} so that it won't ever be reusable. If some class of this
     * session is going to access to lazy fields, it will result in {@link org.hibernate.LazyInitializationException}.
     * This usually should be done when the desktop of the user is going to be recycled (e.g. page refresh).
     *
     * @param desktopId the id of the desktop to find an associated session for, if there is no session found for this
     *                  id, method will result in no-op
     */
    public void closeSession(String desktopId) {
        Session session = sessions.remove(desktopId);
        if (session != null) {
            session.close();
        }
    }

    /**
     * Creates a new session for the specified desktop id if doesn't exist. If it's already inside {@link OpenSessions},
     * then it will be returned. It also sets the flush mode to {@link FlushMode#MANUAL} when session is opened. This
     * method doesn't bind sessions to the thread.
     *
     * @param desktopId an id of the desktop to search the session for
     * @return a session that is bound to the desktop or new session that is not bound to anything if no desktop with
     *         such id was registered
     */
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
