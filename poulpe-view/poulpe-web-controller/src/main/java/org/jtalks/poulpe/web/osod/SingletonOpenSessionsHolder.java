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

import org.hibernate.SessionFactory;

/**
 * Since ZK Listeners in zk.xml can't be manageable by Spring IoC, we have to define a singleton which still is
 * instantiated with Spring, but is accessed in {@link OpenSessionOnDesktopZkListener}.
 *
 * @author stanislav bashkirtsev
 */
public final class SingletonOpenSessionsHolder {
    private static SingletonOpenSessionsHolder HOLDER;
    private final OpenSessions openSessions;

    private SingletonOpenSessionsHolder(SessionFactory sessionFactory) {
        this.openSessions = new OpenSessions(sessionFactory);
    }

    /**
     * A method to instantiate the sessions, it should be called only from Spring context, others should access class
     * via {@link #getOpenSessions()}.
     *
     * @param sessionFactory a session factory to be pushed to {@link OpenSessions}
     * @return a new instance of {@link OpenSessions} that can be used in {@link OpenSessionOnDesktopZkListener}
     */
    public synchronized static OpenSessions instantiate(SessionFactory sessionFactory) {
        if (HOLDER == null) {
            HOLDER = new SingletonOpenSessionsHolder(sessionFactory);
        }
        return HOLDER.openSessions;
    }

    /**
     * Returns the container of open Hibernate sessions, this method does not instantiate anything, you should first
     * initialize class via {@link #instantiate(org.hibernate.SessionFactory)} in Spring Context so that it's possible
     * to return ready-to use {@link OpenSessions}.
     *
     * @return container of sessions with the session factory instantiated in
     *         {@link #instantiate(org.hibernate.SessionFactory)}  by spring context
     * @throws IllegalStateException if the method is accessed before {@link #instantiate(org.hibernate.SessionFactory)}
     *                               was invoked
     */
    public static OpenSessions getOpenSessions() {
        if (HOLDER == null) {
            throw new IllegalStateException("Class should be instantiated with 'instantiate()' method first.");
        }
        return HOLDER.openSessions;
    }
}
