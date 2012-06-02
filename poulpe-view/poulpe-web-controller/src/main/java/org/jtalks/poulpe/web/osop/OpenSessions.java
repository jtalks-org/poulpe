package org.jtalks.poulpe.web.osop;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.access.BeanFactoryLocator;
import org.springframework.beans.factory.access.SingletonBeanFactoryLocator;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
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
            bindToThread(desktopId, session);
        }
    }

    public void closeSession(String desktopId) {
        Session session = sessions.remove(desktopId);
        if (session != null) {
            SessionFactoryUtils.closeSession(session);
        }
    }

    private Session getOrCreateSession(String desktopId) {
        Session session = sessions.get(desktopId);
        if (session == null) {
            session = SessionFactoryUtils.getSession(sessionFactory, true);
        }
        return session;
    }

    private void bindToThread(String desktopId, Session session) {
        TransactionSynchronizationManager.bindResource(desktopId, session);
    }

    private boolean noSessionBoundToThread() {
        return !TransactionSynchronizationManager.hasResource(sessionFactory);
    }
}
