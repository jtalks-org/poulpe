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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

class TableDataInformationProvider {

    TableDataInformationProvider(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Returns the list of all table names which database contains.
     * 
     * @param dbMetaData
     *            An object will be used to fetch information about table names database has.
     * @return a List of Strings where every String instance represents a table name from the database.
     * @throws SQLException
     *             is thrown if there is an error during calaborating with tha database.
     */
    public List<String> getTableNamesList() throws SQLException {
        Connection connection = null;
        ResultSet tablesResultSet = null;
        List<String> tableNames = new ArrayList<String>();
        try {
            connection = getDataSource().getConnection();
            DatabaseMetaData dbMetaData = connection.getMetaData();
            tablesResultSet = dbMetaData.getTables(null, null, null, null);
            while (tablesResultSet.next()) {
                if ("TABLE".equalsIgnoreCase(tablesResultSet.getString("TABLE_TYPE"))) {
                    tableNames.add(tablesResultSet.getString("TABLE_NAME"));
                }
            }
        } finally {
            if (tablesResultSet != null) {
                tablesResultSet.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return tableNames;
    }

    private DataSource getDataSource() {
        return dataSource;
    }

    /**
     * Returns the list of table's columns description. For each column information about column's name, type and other
     * parameters returns. The information is provided in the form ready to be inserted into SQL CREATE TABLE statement.
     * 
     * @return A list of strings where each string represents description of one column.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    public List<TableDataColumn> getColumnDescriptionList(final String tableName)
            throws SQLException {

        List<TableDataColumn> tableColumnList = new ArrayList<TableDataColumn>();
        ResultSet tableMetaData = null;
        Statement stmt = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        Connection connection = null;
        try {
            // Get a list of defaults for the column
            // this cannot be done via ResultSetMetaData, so doing this via tableMetaData instead
            connection = getDataSource().getConnection();
            DatabaseMetaData dbMetaData = connection.getMetaData();
            tableMetaData = dbMetaData.getColumns(null, null, tableName, "%");
            Map<String, String> columnDefaultValues = new HashMap<String, String>();
            while (tableMetaData.next()) {
                String defaultValue = tableMetaData.getString("COLUMN_DEF");
                if (defaultValue != null && defaultValue.length() > 0) {
                    columnDefaultValues.put(tableMetaData.getString("COLUMN_NAME"), defaultValue);
                }
            }

            // Taking the rest of information from ResultSetMetaData object
            stmt = connection.createStatement();
            rs = stmt.executeQuery("SELECT * FROM " + tableName + " WHERE 1 = 0");
            rsmd = rs.getMetaData();
            int numberOfColumns = rsmd.getColumnCount();

            for (int i = 1; i <= numberOfColumns; i++) {
                SqlTypes columnType = SqlTypes.getSqlTypeByJdbcSqlType(rsmd.getColumnType(i));

                TableDataColumn.Builder columnBuilder =
                        new TableDataColumn.Builder(rsmd.getColumnName(i), columnType.toString())
                                .setNullable(rsmd.isNullable(i) == ResultSetMetaData.columnNullable)
                                .setAutoincrement(rsmd.isAutoIncrement(i));
                if (columnDefaultValues.containsKey(rsmd.getColumnName(i))) {
                    columnBuilder.setDefaultValue(columnDefaultValues.get(rsmd.getColumnName(i)));
                }
                if (columnType.isHasSize()) {
                    columnBuilder.setSize(rsmd.getColumnDisplaySize(i));
                }

                tableColumnList.add(columnBuilder.build());
            }

        } finally {
            if (tableMetaData != null) {
                tableMetaData.close();
            }

            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (connection != null) {
                connection.close();
            }

        }
        return tableColumnList;
    }

    // TODO: common part with getPrimaryKeyList() should be moved into common function.
    public List<TableDataForeignKey> getForeignKeyList(final String tableName)
            throws SQLException {

        List<TableDataForeignKey> tableForeignKeyList = new ArrayList<TableDataForeignKey>();
        Connection connection = null;

        try {
            connection = getDataSource().getConnection();
            DatabaseMetaData dbMetaData = connection.getMetaData();
            ResultSet foreignKeys = dbMetaData.getImportedKeys(null, null, tableName);
            while (foreignKeys.next()) {
                if (foreignKeys.getString("FK_NAME") != null) {
                    tableForeignKeyList.add(new TableDataForeignKey(
                            foreignKeys.getString("FK_NAME"),
                            foreignKeys.getString("FKCOLUMN_NAME"),
                            foreignKeys.getString("PKTABLE_NAME"),
                            foreignKeys.getString("PKCOLUMN_NAME")));
                }
            }
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return tableForeignKeyList;
    }

    // TODO: common part with getForeignKeyList() should be moved into common function.
    public List<TableDataPrimaryKey> getPrimaryKeyList(final String tableName)
            throws SQLException {

        List<TableDataPrimaryKey> tablePrimaryKeyList = new ArrayList<TableDataPrimaryKey>();
        Connection connection = null;
        try {
            connection = getDataSource().getConnection();
            DatabaseMetaData dbMetaData = connection.getMetaData();
            ResultSet primaryKeys = dbMetaData.getPrimaryKeys(null, null, tableName);
            while (primaryKeys.next()) {
                if (primaryKeys.getString("PK_NAME") != null) {
                    tablePrimaryKeyList.add(new TableDataPrimaryKey(primaryKeys.getString("COLUMN_NAME")));
                }
            }
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return tablePrimaryKeyList;
    }

    public Map<String, String> getCommonTableParameters(final String tableName) throws SQLException {
        Map<String, String> parameters = new HashMap<String, String>();

        Connection connection = null;
        Statement stmt = null;
        try {
            connection = getDataSource().getConnection();
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SHOW TABLE STATUS WHERE Name="
                    + TableDataUtil.getSqlValueQuotedString(tableName));
            if (rs.next()) {
                for (String column : OTHER_PARAMETER_MAP.keySet()) {
                    String value = rs.getString(column);
                    if (value != null) {
                        parameters.put(OTHER_PARAMETER_MAP.get(column), value);
                    }
                }
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return parameters;
    }

    public List<TableDataDump> getDumpedData(final String tableName) throws SQLException {
        List<TableDataDump> dataDumpList = new ArrayList<TableDataDump>();

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            connection = getDataSource().getConnection();
            stmt = connection.prepareStatement("SELECT * FROM " + TableDataUtil.getSqlColumnQuotedString(tableName));
            rs = stmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, String> dataDump = new HashMap<String, String>();
                String columnValue;
                for (int i = 1; i <= columnCount; i++) {
                    if (rs.getObject(i) != null) {
                        switch (metaData.getColumnType(i)) {
                        case java.sql.Types.CHAR:
                        case java.sql.Types.VARCHAR:
                        case java.sql.Types.LONGVARCHAR:
                        case java.sql.Types.DATE:
                        case java.sql.Types.TIME:
                        case java.sql.Types.TIMESTAMP:
                        case java.sql.Types.NCHAR:
                        case java.sql.Types.NVARCHAR:
                        case java.sql.Types.LONGNVARCHAR:
                        case java.sql.Types.BLOB:
                        case java.sql.Types.CLOB:
                        case java.sql.Types.NCLOB:
                        case java.sql.Types.BINARY:
                        case java.sql.Types.VARBINARY:
                        case java.sql.Types.LONGVARBINARY:
                            columnValue = TableDataUtil.getSqlValueQuotedString(rs.getObject(i).toString());
                            break;

                        case java.sql.Types.NULL:
                            columnValue = "NULL";
                            break;

                        default:
                            columnValue = rs.getObject(i).toString();
                            break;
                        }
                    } else {
                        columnValue = "NULL";
                    }
                    dataDump.put(metaData.getColumnName(i), columnValue);
                }
                dataDumpList.add(new TableDataDump(dataDump));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return dataDumpList;
    }

    private final DataSource dataSource;

    private static final Map<String, String> OTHER_PARAMETER_MAP = new HashMap<String, String>();
    static {
        OTHER_PARAMETER_MAP.put("Engine", "ENGINE");
        OTHER_PARAMETER_MAP.put("Collation", "COLLATE");
        OTHER_PARAMETER_MAP.put("Auto_increment", "AUTO_INCREMENT");
    }

}
