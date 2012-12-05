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
package org.jtalks.poulpe.util.databasebackup.logic.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.jtalks.poulpe.util.databasebackup.domain.ColumnMetaData;
import org.jtalks.poulpe.util.databasebackup.domain.ForeignKey;
import org.jtalks.poulpe.util.databasebackup.domain.UniqueKey;
import org.jtalks.poulpe.util.databasebackup.persistence.DbTable;
import org.jtalks.poulpe.util.databasebackup.persistence.TableDataUtil;

import com.google.common.collect.Lists;

public class SqlTableStructureDump {

    public SqlTableStructureDump(final DbTable dbTable) {
        Validate.notNull(dbTable, "dbTable must not be null");
        this.dbTable = dbTable;
    }

    /**
     * Formats and return the table structure in the shape of SQL statements which can be run lately on SQL console for
     * the table creation.
     * 
     * @return A text based SQL statement for creating the table.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    public StringBuilder dumpStructure() throws SQLException {
        return new StringBuilder()
                .append(getStructureHeaderText())
                .append(getOpenCreateTableStatementText())
                .append(getColumnsDefinitionText(dbTable.getStructure()))
                .append(getTableKeysText())
                .append(getCloseCreateStatementWithTableParamstext());
    }

    /**
     * The method prints a header for the table's exporting structure.
     * 
     * @return A text formated header for the dump file.
     */
    private StringBuilder getStructureHeaderText() {
        return new StringBuilder()
                .append("--" + SqlTableDumpUtil.LINEFEED)
                .append("-- Table structure for table '" + dbTable.getTableName() + "'" + SqlTableDumpUtil.LINEFEED)
                .append("--" + SqlTableDumpUtil.LINEFEED + SqlTableDumpUtil.LINEFEED);
    }

    private StringBuilder getOpenCreateTableStatementText() {
        return new StringBuilder(String.format("CREATE TABLE %s (" + SqlTableDumpUtil.LINEFEED,
                TableDataUtil.getSqlColumnQuotedString(dbTable.getTableName())));
    }

    /**
     * Returns the list of table's columns description. For each column information about column's name, type and other
     * parameters returns. The information is provided in the form ready to be inserted into SQL CREATE TABLE statement.
     * 
     * @return A list of strings where each string represents description of one column.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    private StringBuilder getColumnsDefinitionText(final List<ColumnMetaData> columnDescriptionList)
            throws SQLException {
        assert (columnDescriptionList != null) : "columnDescriptionList must not be null";
        List<String> tableColumnDescriptionList = new ArrayList<String>();

        for (ColumnMetaData column : columnDescriptionList) {
            tableColumnDescriptionList.add(getColumnDescription(column));
        }

        return SqlTableDumpUtil.joinStrings(tableColumnDescriptionList, "," + SqlTableDumpUtil.LINEFEED);
    }

    private StringBuilder getTableKeysText() throws SQLException {
        StringBuilder result = new StringBuilder();

        result.append(performKeyProcessor(new TableKeyProcessor() {
            private final Iterator<UniqueKey> iterator = dbTable.getPrimaryKeyList().iterator();

            @Override
            public boolean hasKeys() throws SQLException {
                return dbTable.getPrimaryKeyList().size() > 0;
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public String next() {
                return String.format(PRIMARY_KEY_TEMPLATE,
                        TableDataUtil.getSqlColumnQuotedString(iterator.next().getColumnName()));
            }
        }));

        result.append(performKeyProcessor(new TableKeyProcessor() {
            private final Iterator<UniqueKey> iterator = dbTable.getUniqueKeyList().iterator();

            @Override
            public boolean hasKeys() throws SQLException {
                return dbTable.getUniqueKeyList().size() > 0;
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public String next() {
                return String.format(UNIQUE_KEY_TEMPLATE,
                        TableDataUtil.getSqlColumnQuotedString(iterator.next().getColumnName()));
            }
        }));

        result.append(performKeyProcessor(new TableKeyProcessor() {
            private final Iterator<ForeignKey> iterator = dbTable.getForeignKeyList().iterator();

            @Override
            public boolean hasKeys() throws SQLException {
                return dbTable.getForeignKeyList().size() > 0;
            }

            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public String next() {
                ForeignKey key = iterator.next();
                return String.format(FOREIGN_KEY_TEMPLATE,
                        TableDataUtil.getSqlColumnQuotedString(key.getFkTableName()),
                        TableDataUtil.getSqlColumnQuotedString(key.getFkColumnName()),
                        TableDataUtil.getSqlColumnQuotedString(key.getPkTableName()),
                        TableDataUtil.getSqlColumnQuotedString(key.getPkColumnName()));
            }
        }));

        return result;
    }

    private StringBuilder getCloseCreateStatementWithTableParamstext() throws SQLException {
        return new StringBuilder(SqlTableDumpUtil.LINEFEED
                + ") " + getOtherTableParameters(dbTable.getCommonParameterMap()) + ";"
                + SqlTableDumpUtil.LINEFEED
                + SqlTableDumpUtil.LINEFEED);
    }

    private String getColumnDescription(final ColumnMetaData column) {
        assert (column != null) : "column must not be null";
        StringBuilder columnDescription = new StringBuilder("    ");
        columnDescription.append(TableDataUtil.getSqlColumnQuotedString(column.getName()) + " " + column.getType());

        if (column.isHasSize()) {
            columnDescription.append("(" + column.getSize() + ")");
        }

        if (column.isNullable()) {
            columnDescription.append(" NULL");
        } else {
            columnDescription.append(" NOT NULL");
        }

        if (column.isHasDefaultValue()) {
            columnDescription.append(" DEFAULT " + column.getDefaultValue());
        }

        if (column.isAutoincrement()) {
            columnDescription.append(" AUTO_INCREMENT");
        }
        return columnDescription.toString();
    }

    private StringBuilder performKeyProcessor(final TableKeyProcessor processor) throws SQLException {
        assert (processor != null) : "processor must not be null";
        StringBuilder result = new StringBuilder();
        if (processor.hasKeys()) {
            result.append("," + SqlTableDumpUtil.LINEFEED);
            List<String> tableKeyList = Lists.newArrayList();
            while (processor.hasNext()) {
                tableKeyList.add(processor.next());
            }
            result.append(SqlTableDumpUtil.joinStrings(tableKeyList, "," + SqlTableDumpUtil.LINEFEED));
        }
        return result;
    }

    /**
     * Formats and returns additional parameters which are needed for the table creation (see
     * {@link #getTableStructure()}). Currently the method returns ENGINE, COLLATE and AUTO_INCREMENT keys for the
     * CREATE TABLE statement. The method returns only those key which defined for the table.
     * 
     * @return A string which contains additional parameters for the table creating and their values.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    private String getOtherTableParameters(final Map<String, String> parameters) throws SQLException {
        assert (parameters != null) : "parameters must not be null";
        StringBuilder otherTableParameters = new StringBuilder();
        for (String key : parameters.keySet()) {
            otherTableParameters.append(key + "=" + parameters.get(key) + " ");
        }
        return otherTableParameters.toString();
    }

    private interface TableKeyProcessor {
        boolean hasKeys() throws SQLException;

        boolean hasNext();

        String next();
    }

    private final DbTable dbTable;

    /**
     * %1 - primaryKey.getColumnName().
     */
    private static final String PRIMARY_KEY_TEMPLATE = "    PRIMARY KEY (%s)";
    /**
     * %1 - uniqueKey.getColumnName().
     */
    private static final String UNIQUE_KEY_TEMPLATE = "    CONSTRAINT %1s UNIQUE (%1$s)";
    /**
     * %1 - getFkTableName %2 - getFkColumnName %3 - getPkTableName %4 - getPkColumnName.
     */
    private static final String FOREIGN_KEY_TEMPLATE = "    KEY %1$s(%2$s)," + SqlTableDumpUtil.LINEFEED
            + "    CONSTRAINT %1$s FOREIGN KEY (%2$s) REFERENCES %3$s(%4$s)";
}
