package org.jtalks.poulpe.web.osop;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author stanislav bashkirtsev
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
            sessions.put(desktopId, session);
            bindToThread(session);
        }
    }

    public void unbindSession(){
        TransactionSynchronizationManager.unbindResource(sessionFactory);
    }

    public void closeSession(String desktopId) {
        Session session = sessions.remove(desktopId);
        if (session != null) {
//            SessionFactoryUtils.processDeferredClose(sessionFactory);
//            SessionFactoryUtils.closeSession(session);
//            TransactionSynchronizationManager.unbindResource(sessionFactory);
            session.close();
        }
    }

    private Session getOrCreateSession(String desktopId) {
        Session session = sessions.get(desktopId);
        if (session == null) {
            session = SessionFactoryUtils.getSession(sessionFactory, true);
            session.setFlushMode(FlushMode.MANUAL);
        }
        return session;
    }

    private void bindToThread(Session session) {
        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
//        SessionFactoryUtils.initDeferredClose(sessionFactory);
    }

    private boolean noSessionBoundToThread() {
//        return !SessionFactoryUtils.isDeferredCloseActive(sessionFactory);
        return !TransactionSynchronizationManager.hasResource(sessionFactory);
    }
}
