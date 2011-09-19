/**
 * Copyright (C) 2011  jtalks.org Team
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
 * Also add information on how to contact you by electronic and paper mail.
 * Creation date: Apr 12, 2011 / 8:05:19 PM
 * The jtalks.org Project
 */
package org.jtalks.poulpe.web.controller;


/**
 * An event listener that will be notified when CRUD or cancelled event occurs
 * 
 * @author Vladimir Bukhtoyarov
 * 
 * @param <T>
 */
public interface EditListener<T> {

    /**
     * Event occurring during the creating of entity.
     * 
     * @param entity created persistent object
     */
    void onCreate(T entity);

    /**
     * Event occurring during the removal of entity.
     * 
     * @param entity deleted persistent object
     */
    void onDelete(T entity);

    /**
     * Event occurring during the updating of entity.
     * 
     * @param entity
     */
    void onUpdate(T entity);

    /**
     * Events occurs when the user canceled editing
     */
    void onCloseEditorWithoutChanges();

}
