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

/**
 * The listener which should be used in case of a high probability of concurrent
 * data modifications by several users
 * 
 * @author Vladimir Bukhtoyarov
 * 
 * @param <T>
 */
public abstract class HighConcurrencyEditListener<T> implements EditListener<T> {

    /** {@inheritDoc} */
    @Override
    public void onCreate(T entity) {
        refreshData();
    }

    /** {@inheritDoc} */
    @Override
    public void onDelete(T entity) {
        refreshData();
    }

    /** {@inheritDoc} */
    @Override
    public void onUpdate(T entity) {
        refreshData();
    }

    /** {@inheritDoc} */
    @Override
    public void onCloseEditorWithoutChanges() {
        refreshData();
    }

    /**
     * Action to be reformed on every event
     */
    protected abstract void refreshData();

}
