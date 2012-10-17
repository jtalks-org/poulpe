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
 * The class represent a Foreign key description data object. The class is immutable.
 * 
 * @author Evgeny Surovtsev
 * 
 */
final class TableDataForeignKey {

    /**
     * Initiate an instance of the class with a given Foreign Key information.
     * 
     * @param fkTableName
     *            A Table which contain Foreign key.
     * @param fkColumnName
     *            A Foreign Key value.
     * @param pkTableName
     *            A Table which contain Primary key.
     * @param pkColumnName
     *            A Primary Key value.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    public TableDataForeignKey(final String fkTableName, final String fkColumnName, final String pkTableName,
            final String pkColumnName) throws SQLException {
        this.fkTableName = fkTableName;
        this.fkColumnName = fkColumnName;
        this.pkTableName = pkTableName;
        this.pkColumnName = pkColumnName;
    }

    /**
     * Returns a foreign key table name.
     * 
     * @return Foreign table name.
     */
    public String getFkTableName() {
        return fkTableName;
    }

    /**
     * Returns a foreign key value.
     * 
     * @return Foreign key.
     */
    public String getFkColumnName() {
        return fkColumnName;
    }

    /**
     * Returns a primary key table name.
     * 
     * @return Primary table name.
     */
    public String getPkTableName() {
        return pkTableName;
    }

    /**
     * Returns a primary key value.
     * 
     * @return Primary key.
     */
    public String getPkColumnName() {
        return pkColumnName;
    }

    private final String fkTableName;
    private final String fkColumnName;
    private final String pkTableName;
    private final String pkColumnName;
}
