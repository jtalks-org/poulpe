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
package org.jtalks.poulpe.model.databasebackup.dto;

import org.apache.commons.lang3.Validate;

/**
 * The class represent a Primary or Unique key description data object. The class is immutable.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public final class UniqueKey implements TableKey {
    /**
     * Initiate an instance of the class with a given Primary Key value.
     * 
     * @param columnName
     *            A String that represents Primary Key value.
     * @throws NullPointerException
     *             If pkColumnName is null.
     */
    public UniqueKey(final String columnName) {
        Validate.notNull(columnName, "columnName must not be null");
        this.columnName = columnName;
    }

    /**
     * Returns a primary key value.
     * 
     * @return Primary key.
     */
    public String getColumnName() {
        return columnName;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (columnName == null ? 0 : columnName.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "UniqueKey=" + columnName;
    }

    @Override
    public boolean equals(final Object obj) {
        return this == obj || columnName != null && obj instanceof UniqueKey
                && columnName.equals(((UniqueKey) obj).columnName);
    }

    private final String columnName;
}
