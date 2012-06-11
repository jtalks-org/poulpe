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
package org.jtalks.poulpe.web.controller.utils;

import org.jtalks.poulpe.web.controller.SelectedEntity;

/**
 * @author unascribed
 * @author Vyacheslav Zhivaev
 */
public class ObjectsFactory {

    /**
     * Create {@link SelectedEntity} instance with predefined internal {@code SelectedEntity.entity}.
     * 
     * @param entity the entity to set
     * @return new instance of {@link SelectedEntity}
     */
    public static <T> SelectedEntity<T> createSelectedEntity(T entity) {
        SelectedEntity<T> result = new SelectedEntity<T>();
        result.setEntity(entity);
        return result;
    }
}
