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
import java.util.ArrayList;
import java.util.List;

import org.jtalks.poulpe.model.databasebackup.dto.Cell;
import org.jtalks.poulpe.model.databasebackup.dto.Row;
import org.jtalks.poulpe.model.databasebackup.jdbc.DbTable;
import org.jtalks.poulpe.model.databasebackup.jdbc.TableDataUtil;

public class SqlTableDataDump {

    public SqlTableDataDump(final DbTable dbTable) {
        if (dbTable == null) {
            throw new NullPointerException("dbTable cannot be null.");
        }
        this.dbTable = dbTable;
    }

    /**
     * Formats and return the table's actual data in the shape of SQL statements which can be run lately on SQL console
     * for inserting exported data into the table.
     * 
     * @return A text based SQL statements for inserting exported data into the table.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    public StringBuilder dumpData() throws SQLException {
        return new StringBuilder()
                .append(getDumpedDataHeaderText())
                .append(getTableDataText())
                .append(SqlTableDumpUtil.LINEFEED + SqlTableDumpUtil.LINEFEED);
    }

    /**
     * The method prints a header for the table's exporting data.
     * 
     * @return A text formated header for the dump file.
     */
    private StringBuilder getDumpedDataHeaderText() {
        return new StringBuilder()
                .append("--" + SqlTableDumpUtil.LINEFEED)
                .append("-- Dumping data for table '" + dbTable.getTableName() + "'" + SqlTableDumpUtil.LINEFEED)
                .append("--" + SqlTableDumpUtil.LINEFEED + SqlTableDumpUtil.LINEFEED);
    }

    private StringBuilder getTableDataText() throws SQLException {
        StringBuilder result = new StringBuilder();
        for (Row dataDump : dbTable.getData()) {
            result.append(getRowDataText(dataDump));
        }
        return result;

    }

    private StringBuilder getRowDataText(final Row dataDump) {
        List<String> nameColumns = new ArrayList<String>();
        List<String> valueColumns = new ArrayList<String>();

        for (Cell columnData : dataDump.getCellList()) {
            String value = (columnData.getColumnData() != null) ? columnData.getColumnData().toString() : "NULL";
            if (columnData.getColumnData() != null && columnData.getSqlType().isTextBased()) {
                value = TableDataUtil.getSqlValueQuotedString(value);
            }
            valueColumns.add(value);
            nameColumns.add(columnData.getColumnName());
        }

        return new StringBuilder()
                .append(String.format(INSERT_ROW_TEMPLATE,
                        TableDataUtil.getSqlColumnQuotedString(dbTable.getTableName()),
                        SqlTableDumpUtil.joinStrings(nameColumns, ","),
                        SqlTableDumpUtil.joinStrings(valueColumns, ",")))
                .append(SqlTableDumpUtil.LINEFEED);
    }

    private final DbTable dbTable;

    /**
     * %1 - tablename %2 - nameColumns %3 - valueColumns.
     */
    private static final String INSERT_ROW_TEMPLATE = "INSERT INTO %1$s (%2$s) VALUES (%3$s);";
}
