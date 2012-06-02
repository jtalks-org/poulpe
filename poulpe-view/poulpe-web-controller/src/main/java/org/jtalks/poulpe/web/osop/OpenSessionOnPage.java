package org.jtalks.poulpe.web.osop;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.HibernateAccessor;
import org.springframework.orm.hibernate3.SessionFactoryUtils;

/**
 * @author stanislav bashkirtsev
 */
public class OpenSessionOnPage extends HibernateAccessor {
    public OpenSessionOnPage(SessionFactory sessionFactory) {
        setFlushMode(FLUSH_NEVER);
        setSessionFactory(sessionFactory);
    }

    public void startSession() {
        if (!SessionFactoryUtils.isDeferredCloseActive(getSessionFactory())) {
            SessionFactoryUtils.initDeferredClose(getSessionFactory());
        }
    }

    public void closeSession() {
        SessionFactoryUtils.processDeferredClose(getSessionFactory());
    }
}
