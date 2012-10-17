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
package org.jtalks.poulpe.logic.databasebackup.impl;

import java.sql.SQLException;

/**
 * The class represent a Primary key description data object. The class is immutable.
 * 
 * @author Evgeny Surovtsev
 * 
 */
final class TableDataPrimaryKey {
    /**
     * Initiate an instance of the class with a given Primary Key value.
     * 
     * @param pkColumnName
     *            A String that represents Primary Key value.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    public TableDataPrimaryKey(final String pkColumnName) throws SQLException {
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

    private final String pkColumnName;
}
