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
package org.jtalks.poulpe.util.databasebackup.dbdump.mysqlsyntax;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.jtalks.poulpe.util.databasebackup.dbdump.HeaderAndDataAwareCommand;
import org.jtalks.poulpe.util.databasebackup.domain.Cell;
import org.jtalks.poulpe.util.databasebackup.domain.Row;
import org.jtalks.poulpe.util.databasebackup.persistence.DbTable;
import org.jtalks.poulpe.util.databasebackup.persistence.TableDataUtil;

public class TableDataDumpCommand extends HeaderAndDataAwareCommand {

    public TableDataDumpCommand(final DbTable dbTable) {
        Validate.notNull(dbTable, "dbTable must not be null");
        this.dbTable = dbTable;
    }

    @Override
    protected StringBuilder getHeader() {
        StringBuilder header = new StringBuilder();
        header.append("--").append(LINEFEED);
        header.append("-- Dumping data for table ");
        header.append(TableDataUtil.getSqlColumnQuotedString(dbTable.getTableName()));
        header.append(LINEFEED);
        header.append("--").append(LINEFEED).append(LINEFEED);
        return header;
    }

    @Override
    protected StringBuilder getData() throws SQLException {
        StringBuilder data = new StringBuilder();
        for (Row dataDump : dbTable.getData()) {
            data.append(getRowDataText(dataDump));
        }
        return data;
    }

    /**
     * Formats and returns a SQL statement for inserting into a table given Row object.
     * 
     * @param row
     *            A Row based on which a new INSERT statement will be constructed.
     * @return A SQL valid INSERT statement.
     */
    private StringBuilder getRowDataText(final Row row) {
        assert row != null : "row must not be null";
        List<String> nameColumns = new ArrayList<String>();
        List<String> valueColumns = new ArrayList<String>();

        for (Cell columnData : row.getCellList()) {
            String value = columnData.getColumnData() != null ? columnData.getColumnData().toString() : "NULL";
            if (columnData.getColumnData() != null && columnData.getSqlType().isTextBased()) {
                value = TableDataUtil.getSqlValueQuotedString(value);
            }
            valueColumns.add(value);
            nameColumns.add(columnData.getColumnName());
        }

        return new StringBuilder()
                .append(String.format(INSERT_ROW_TEMPLATE,
                        TableDataUtil.getSqlColumnQuotedString(dbTable.getTableName()),
                        StringUtils.join(nameColumns, ","),
                        StringUtils.join(valueColumns, ",")))
                .append(LINEFEED);
    }

    private final DbTable dbTable;

    /**
     * %1 - tablename %2 - nameColumns %3 - valueColumns.
     */
    private static final String INSERT_ROW_TEMPLATE = "INSERT INTO %1$s (%2$s) VALUES (%3$s);";
}
