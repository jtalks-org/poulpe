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

import org.jtalks.poulpe.model.databasebackup.jdbc.DbTable;

/**
 * The class represents a full table dump and is used to export all information (structure, data) about concrete table
 * from the database into text based SQL commands shape.
 * 
 * @author Evgeny Surovtsev
 * 
 */
class SqlTableDump {
    private final DbTable dbTable;

    /**
     * The constructor creates a text based representation of the table with the same name as the name given in the
     * constructor's parameters. For that constructor successively calls Create Table Structure (see
     * {@link #getTableStructure()}) and Export data (see {@link #getDumpedData()}) methods.
     * 
     * @param dbTable
     * @throws NullPointerException
     *             if any of dataSource or tableName is null.
     */
    public SqlTableDump(final DbTable dbTable) {
        if (dbTable == null) {
            throw new NullPointerException("dbTable cannot be null.");
        }
        this.dbTable = dbTable;
    }

    public String getFullDump() throws SQLException {
        return new StringBuilder()
                .append(new SqlTableStructureDump(dbTable).dumpStructure())
                .append(new SqlTableDataDump(dbTable).dumpData())
                .toString();
    }
}
