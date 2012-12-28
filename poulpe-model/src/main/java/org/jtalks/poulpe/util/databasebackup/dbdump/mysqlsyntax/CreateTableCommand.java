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
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.jtalks.poulpe.util.databasebackup.dbdump.HeaderAndDataAwareCommand;
import org.jtalks.poulpe.util.databasebackup.domain.ColumnMetaData;
import org.jtalks.poulpe.util.databasebackup.domain.UniqueKey;
import org.jtalks.poulpe.util.databasebackup.persistence.DbTable;
import org.jtalks.poulpe.util.databasebackup.persistence.TableDataUtil;

import com.google.common.collect.Lists;

public class CreateTableCommand extends HeaderAndDataAwareCommand {

    public CreateTableCommand(final DbTable dbTable) {
        Validate.notNull(dbTable, "dbTable must not be null");
        this.dbTable = dbTable;
    }

    @Override
    protected StringBuilder getHeader() {
        StringBuilder header = new StringBuilder();
        header.append("--");
        header.append(LINEFEED);
        header.append("-- Table structure for table ");
        header.append(TableDataUtil.getSqlColumnQuotedString(dbTable.getTableName()));
        header.append(LINEFEED);
        header.append("--");
        header.append(LINEFEED);

        return header;
    }

    @Override
    protected StringBuilder getData() throws SQLException {
        StringBuilder data = new StringBuilder();

        data.append("CREATE TABLE ");
        data.append(TableDataUtil.getSqlColumnQuotedString(dbTable.getTableName()));
        data.append(" (");
        data.append(LINEFEED);

        data.append(getColumns());
        data.append(getKeys(dbTable.getPrimaryKeySet(), PRIMARY_KEY_TEMPLATE));
        data.append(getKeys(dbTable.getUniqueKeySet(), UNIQUE_KEY_TEMPLATE));

        data.append(LINEFEED);
        data.append(") ");
        data.append(getCommonParameters());
        data.append(";");

        return data;
    }

    /**
     * Formats and returns additional parameters which are needed for the table creation (see
     * {@link #getTableStructure()}). Currently the method returns ENGINE, COLLATE and AUTO_INCREMENT keys for the
     * CREATE TABLE statement. The method returns only those key which defined for the table.
     * 
     * @param parameters
     *            a parameters map where key is a parameter's name and value is a parameter's value.
     * @return A string which contains additional parameters for the table creating and their values.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    private StringBuilder getCommonParameters() throws SQLException {
        Map<String, String> parameters = dbTable.getCommonParameterMap();
        StringBuilder otherTableParameters = new StringBuilder();
        for (String key : parameters.keySet()) {
            otherTableParameters.append(" " + key + "=" + parameters.get(key));
        }
        return otherTableParameters;
    }

    /**
     * Returns the list of table's columns description. For each column information about column's name, type and other
     * parameters returns. The information is provided in the form ready to be inserted into SQL CREATE TABLE statement.
     * 
     * @param columnDescriptionList
     *            a list of ColumnMetaData each of which describes and represents one column in the database.
     * @return A list of strings where each string represents description of one column.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    private String getColumns() throws SQLException {
        List<String> tableColumnDescriptionList = Lists.newArrayList();
        for (ColumnMetaData column : dbTable.getStructure()) {
            tableColumnDescriptionList.add(getColumnDescription(column));
        }

        return StringUtils.join(tableColumnDescriptionList, "," + LINEFEED);
    }

    /**
     * Formats and returns a SQL representation of column based on given ColumnMetaData object.
     * 
     * @param column
     *            a description of column based on which an SQL representation will be built.
     * @return SQL representation of the column.
     */
    private String getColumnDescription(final ColumnMetaData column) {

        assert column != null : "column must not be null";
        StringBuilder columnDescription = new StringBuilder("    ");
        columnDescription.append(TableDataUtil.getSqlColumnQuotedString(column
                .getName()) + " " + column.getType());

        if (column.isHasSize()) {
            columnDescription.append("(" + column.getSize() + ")");
        }

        if (column.isNullable()) {
            columnDescription.append(" NULL");
        } else {
            columnDescription.append(" NOT NULL");
        }

        if (column.isHasDefaultValue()) {
            columnDescription.append(" DEFAULT ");
            String defaultValue = column.getDefaultValue();
            if (column.getType().isTextBased()
                    && !column.getType().getKeyWordList()
                            .contains(defaultValue)) {
                defaultValue = TableDataUtil
                        .getSqlValueQuotedString(defaultValue);
            }
            columnDescription.append(defaultValue);
        }

        if (column.isAutoincrement()) {
            columnDescription.append(" AUTO_INCREMENT");
        }

        if (column.hasComment()) {
            columnDescription.append(" COMMENT ");
            columnDescription.append(TableDataUtil
                    .getSqlValueQuotedString(column.getComment()));
        }

        return columnDescription.toString();
    }

    /**
     * Formats string representation of the table keys (Primary, foreign and unique) so it can be used in the SQL CREATE
     * TABLE statement.
     * 
     * @return a string representation of the table keys
     * @throws SQLException
     *             if any error with database occurs
     */
    private StringBuilder getKeys(final Set<UniqueKey> keySet, final String template)
            throws SQLException {
        StringBuilder result = new StringBuilder();

        if (keySet.size() > 0) {
            result.append(", ");
            result.append(LINEFEED);

            List<String> tableKeyList = Lists.newArrayList();
            for (UniqueKey key : keySet) {
                List<String> quotedKeys = Lists.newArrayList();
                for (String unquotedKey : key.getColumnNameSet()) {
                    quotedKeys.add(TableDataUtil.getSqlColumnQuotedString(unquotedKey));
                }
                tableKeyList.add(String.format(template, key.getIndexName(), StringUtils.join(quotedKeys, ", ")));
            }
            result.append(StringUtils.join(tableKeyList, ", " + LINEFEED));
        }

        return result;
    }

    private final DbTable dbTable;

    /**
     * %1 - primaryKey.getColumnName().
     */
    private static final String PRIMARY_KEY_TEMPLATE = "    PRIMARY KEY (%2$s)";
    /**
     * %1 - uniqueKey.getColumnName().
     */
    private static final String UNIQUE_KEY_TEMPLATE = "    CONSTRAINT %1$s UNIQUE (%2$s)";
}
