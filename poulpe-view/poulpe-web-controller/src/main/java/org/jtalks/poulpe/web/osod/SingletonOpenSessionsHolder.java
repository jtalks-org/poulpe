package org.jtalks.poulpe.web.osod;

import org.hibernate.SessionFactory;

/**
 * @author stanislav bashkirtsev
 */
public class SingletonOpenSessionsHolder {
    private static SingletonOpenSessionsHolder HOLDER;
    private final OpenSessions openSessions;

    private SingletonOpenSessionsHolder(SessionFactory sessionFactory) {
        this.openSessions = new OpenSessions(sessionFactory);
    }

    public static OpenSessions instantiate(SessionFactory sessionFactory) {
        if (HOLDER == null) {
            HOLDER = new SingletonOpenSessionsHolder(sessionFactory);
        }
        return HOLDER.openSessions;
    }

    public static OpenSessions getOpenSessions() {
        return HOLDER.openSessions;
    }
}
