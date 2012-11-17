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

import org.apache.commons.lang3.Validate;
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
     * Creates a new SqlDumpObject with provided source point - DbTable.
     * 
     * @param dbTable
     *            A source of database data.
     * @throws NullPointerException
     *             if any of dataSource or tableName is null.
     */
    public SqlTableDump(final DbTable dbTable) {
        Validate.notNull(dbTable, "dbTable must not be null");
        this.dbTable = dbTable;
    }

    /**
     * Returns a full dump of the table including table structure and table data in the shape of SQL statements.
     * 
     * @return SQL statements for creating a table and inserting data into it.
     * @throws SQLException
     *             if any of dataSource or tableName is null.
     */
    public String getFullDump() throws SQLException {
        return new StringBuilder()
                .append(new SqlTableStructureDump(dbTable).dumpStructure())
                .append(new SqlTableDataDump(dbTable).dumpData())
                .toString();
    }
}
