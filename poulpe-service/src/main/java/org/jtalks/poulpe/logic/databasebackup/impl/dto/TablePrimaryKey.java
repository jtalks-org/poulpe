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
package org.jtalks.poulpe.logic.databasebackup.impl.dto;

/**
 * The class represent a Primary key description data object. The class is immutable.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public final class TablePrimaryKey {
    /**
     * Initiate an instance of the class with a given Primary Key value.
     * 
     * @param pkColumnName
     *            A String that represents Primary Key value.
     */
    public TablePrimaryKey(final String pkColumnName) {
        if (pkColumnName == null) {
            throw new NullPointerException("pkColumnName parameter cannot be null.");
        }
        this.pkColumnName = pkColumnName;
    }

    /**
     * Returns a primary key value.
     * 
     * @return Primary key.
     */
    public String getPkColumnName() {
        return pkColumnName;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + ((pkColumnName == null) ? 0 : pkColumnName.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "TablePrimaryKey=" + pkColumnName;
    }

    @Override
    public boolean equals(final Object obj) {
        return (this == obj)
                || (pkColumnName != null
                        && obj instanceof TablePrimaryKey && pkColumnName.equals(((TablePrimaryKey) obj).pkColumnName));
    }

    private final String pkColumnName;
}
