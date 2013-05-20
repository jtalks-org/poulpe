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
package org.jtalks.poulpe.util.databasebackup.dbdump.mysql;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.jtalks.poulpe.util.databasebackup.dbdump.HeaderAndDataAwareCommand;
import org.jtalks.poulpe.util.databasebackup.domain.ColumnMetaData;
import org.jtalks.poulpe.util.databasebackup.domain.Row;
import org.jtalks.poulpe.util.databasebackup.exceptions.RowProcessingException;
import org.jtalks.poulpe.util.databasebackup.persistence.DbTable;
import org.jtalks.poulpe.util.databasebackup.persistence.RowProcessor;
import org.jtalks.poulpe.util.databasebackup.persistence.TableDataUtil;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;
import java.util.List;
import java.util.Map.Entry;

/**
 * Class is a command (see {@link org.jtalks.poulpe.util.databasebackup.dbdump.DbDumpCommand} for details) which
 * generates INSERT statements for table using MySQL syntax.
 * 
 * @author Evgeny Surovtsev
 * 
 */
class TableDataDumpCommand extends HeaderAndDataAwareCommand {
    /**
     * Initializes a TableDataDump command with given DbTable as a data provider.
     * 
     * @param dbTable
     *            a data provider for generating command's results.
     */
    public TableDataDumpCommand(DbTable dbTable) {
        Validate.notNull(dbTable, "dbTable must not be null");
        this.dbTable = dbTable;
    }

    @Override
    protected void putHeader(Writer writer) throws IOException {
        assert writer != null;
        StringBuilder header = new StringBuilder();
        header.append("--").append(LINEFEED);
        header.append("-- Dumping data for table ");
        header.append(TableDataUtil.getSqlColumnQuotedString(dbTable.getTableName()));
        header.append(LINEFEED);
        header.append("--").append(LINEFEED).append(LINEFEED);

        writer.write(header.toString());
    }

    @Override
    protected void putData(final Writer writer) throws SQLException, IOException {
        assert writer != null;
        dbTable.getData(new RowProcessor() {
            @Override
            public void process(Row rowToProcess) throws RowProcessingException {
                try {
                    writer.write(getRowDataText(rowToProcess));
                } catch (IOException e) {
                    throw new RowProcessingException(e);
                }
            }
        });
    }

    /**
     * Formats and returns a SQL statement for inserting into a table given Row object.
     * 
     * @param row
     *            A Row based on which a new INSERT statement will be constructed.
     * @return A SQL valid INSERT statement.
     */
    private String getRowDataText(Row row) {
        assert row != null;
        List<String> nameColumns = Lists.newLinkedList();
        List<String> valueColumns = Lists.newLinkedList();

        for (Entry<ColumnMetaData, Object> columnEntry : row.getRowSet()) {
            ColumnMetaData columnMetaData = columnEntry.getKey();
            String value = ObjectUtils.toString(columnEntry.getValue(), "NULL");
            if (columnEntry.getValue() != null && columnMetaData.getType().isTextBased()) {
                value = TableDataUtil.getSqlValueQuotedString(value);
            }
            valueColumns.add(value);
            nameColumns.add(columnMetaData.getName());
        }

        return String.format(INSERT_ROW_TEMPLATE,
                TableDataUtil.getSqlColumnQuotedString(dbTable.getTableName()),
                StringUtils.join(nameColumns, ","),
                StringUtils.join(valueColumns, ","));
    }

    private final DbTable dbTable;

    /**
     * %1 - tablename %2 - nameColumns %3 - valueColumns.
     */
    private static final String INSERT_ROW_TEMPLATE = "INSERT INTO %1$s (%2$s) VALUES (%3$s);" + LINEFEED;
}
