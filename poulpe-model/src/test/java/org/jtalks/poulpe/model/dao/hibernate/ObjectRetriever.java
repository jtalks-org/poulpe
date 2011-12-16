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
package org.jtalks.poulpe.model.dao.hibernate;

import org.hibernate.Session;
import org.jtalks.common.model.entity.Entity;

/**
 * @author Alexey Grigorev
 */
public class ObjectRetriever {
    
    /**
     * Retrieves the actual object stored in the database, clearing session's cache for 
     * ensuring the object is brand new.
     * 
     * @param object to retrieve
     * @param session Hibernate session
     * @return brand new retrieved object for database
     */
    public static <E extends Entity> E retrieveUpdated(E object, Session session) { 
        session.evict(object);
        return retrieve(object, session);
    }

    /**
     * Retrieves the actual object stored in the database
     */
    @SuppressWarnings("unchecked")
    public static <E extends Entity> E retrieve(E object, Session session) { 
        return (E) session.get(object.getClass(), object.getId());
    }
    
}
