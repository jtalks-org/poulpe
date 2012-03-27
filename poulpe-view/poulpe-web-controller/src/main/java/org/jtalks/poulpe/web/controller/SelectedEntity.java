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
package org.jtalks.poulpe.web.controller;

import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;

/**
 * This is DTO which represents state of currently selected in view entity. Instance's of this class managed by
 * application context. It's can be used for cross-component interaction, i.e. transfer entity's from one VM to another.
 * 
 * @author Vyacheslav Zhivaev
 */
@ThreadSafe
public class SelectedEntity<E> {

    private volatile E entity = null;

    /**
     * Gets currently selected entity.
     * 
     * @return the selected entity, can return {@code null} if nothing was already selected
     */
    @Nullable
    public synchronized E getEntity() {
        return entity;
    }

    /**
     * Sets entity which currently selected in UI.
     * 
     * @param entity the new instance entity to set, can be {@code null} for omitting previous data
     */
    public synchronized void setEntity(E entity) {
        this.entity = entity;
    }

}
