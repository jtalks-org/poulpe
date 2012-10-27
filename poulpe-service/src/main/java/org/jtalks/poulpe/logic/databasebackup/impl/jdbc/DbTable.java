package org.jtalks.poulpe.logic.databasebackup.impl.jdbc;

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

import org.jtalks.poulpe.logic.databasebackup.impl.dto.SqlTypes;
import org.jtalks.poulpe.logic.databasebackup.impl.dto.TableColumn;
import org.jtalks.poulpe.logic.databasebackup.impl.dto.TableForeignKey;
import org.jtalks.poulpe.logic.databasebackup.impl.dto.TablePrimaryKey;
import org.jtalks.poulpe.logic.databasebackup.impl.dto.TableRow;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * The class represents a database table and provides specific table's information like table's structure and data.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class DbTable {
    /**
     * Initialize the class with given Data Source and Table Name.
     * 
     * @param dataSource
     *            A datasource to access to the database.
     * @param tableName
     *            A table name information about will be returned by the instance.
     */
    public DbTable(final DataSource dataSource, final String tableName) {
        this.tableName = tableName;
        this.dataSource = dataSource;
    }

    /**
     * The method obtains and returns table rows data for the given table.
     * 
     * @return A list of obtained rows ({@link TableRow}).
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    public List<TableRow> getData() throws SQLException {
        if (tableDataList == null) {
            tableDataList = obtainTableDataList();
        }
        return tableDataList;
    }

    /**
     * The method returns table's data in the shape of list of TableRow.
     * 
     * @return List of table rows.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    private List<TableRow> obtainTableDataList() throws SQLException {
        List<TableRow> dataDumpList = Lists.newArrayList();

        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            connection = dataSource.getConnection();
            stmt = connection.prepareStatement("SELECT * FROM " + TableDataUtil.getSqlColumnQuotedString(tableName));
            rs = stmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();

            int columnCount = metaData.getColumnCount();
            while (rs.next()) {
                Map<String, String> dataDump = Maps.newHashMap();
                String columnValue;
                for (int i = 1; i <= columnCount; i++) {
                    if (rs.getObject(i) != null) {
                        SqlTypes columnType = SqlTypes.getSqlTypeByJdbcSqlType(metaData.getColumnType(i));

                        if (columnType == SqlTypes.NULL) {
                            columnValue = "NULL";
                        } else if (columnType.isTextBased()) {
                            columnValue = TableDataUtil.getSqlValueQuotedString(rs.getObject(i).toString());
                        } else {
                            columnValue = rs.getObject(i).toString();
                        }
                    } else {
                        columnValue = "NULL";
                    }
                    dataDump.put(metaData.getColumnName(i), columnValue);
                }
                TableRow row = new TableRow();
                for (String column : dataDump.keySet()) {
                    row.addColumn(column, dataDump.get(column));
                }
                dataDumpList.add(row);
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

    /**
     * Returns the list of table's columns description. For each column information about column's name, type and other
     * parameters returns. The information is provided in the form ready to be inserted into SQL CREATE TABLE statement.
     * 
     * @return A list of strings where each string represents description of one column.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    public List<TableColumn> getStructure() throws SQLException {
        if (tableStructureList == null) {
            tableStructureList = obtainTableStructureList();
        }
        return Lists.newArrayList(tableStructureList);
    }

    /**
     * Returns the structure of the table in the shape of list of Table columns.
     * 
     * @return A list of Table column elements.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    private List<TableColumn> obtainTableStructureList() throws SQLException {
        List<TableColumn> tableColumnList = new ArrayList<TableColumn>();
        ResultSet tableMetaData = null;
        Statement stmt = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        Connection connection = null;
        try {
            // Get a list of defaults for the column
            // this cannot be done via ResultSetMetaData, so doing this via tableMetaData instead
            connection = dataSource.getConnection();
            DatabaseMetaData dbMetaData = connection.getMetaData();
            tableMetaData = dbMetaData.getColumns(null, null, tableName, "%");
            Map<String, String> columnDefaultValues = Maps.newHashMap();
            while (tableMetaData.next()) {
                String defaultValue = tableMetaData.getString("COLUMN_DEF");
                if (defaultValue != null && defaultValue.length() > 0) {
                    columnDefaultValues.put(tableMetaData.getString("COLUMN_NAME"), defaultValue);
                }
            }

            // Taking the rest of information from ResultSetMetaData object
            stmt = connection.createStatement();
            // WHERE 1 = 0 -- we don't need actual data, just a table structure, so lets make the query's result empty.
            rs = stmt.executeQuery("SELECT * FROM " + tableName + " WHERE 1 = 0");
            rsmd = rs.getMetaData();
            int numberOfColumns = rsmd.getColumnCount();

            for (int i = 1; i <= numberOfColumns; i++) {
                SqlTypes columnType = SqlTypes.getSqlTypeByJdbcSqlType(rsmd.getColumnType(i));

                TableColumn column = new TableColumn(rsmd.getColumnName(i), columnType)
                        .setNullable(rsmd.isNullable(i) == ResultSetMetaData.columnNullable)
                        .setAutoincrement(rsmd.isAutoIncrement(i));
                if (columnDefaultValues.containsKey(rsmd.getColumnName(i))) {
                    column.setDefaultValue(columnDefaultValues.get(rsmd.getColumnName(i)));
                }
                if (columnType.isHasSize()) {
                    column.setSize(rsmd.getColumnDisplaySize(i));
                }

                tableColumnList.add(column);
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

    /**
     * Returns the map of additional table's parameters such as table's engine, collation, etc. Currently the checked
     * parameters are: Engine, Collation, Auto_increment.
     * 
     * @return A map of Parameter name - Parameter value pair for the given table.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    public Map<String, String> getCommonParameterMap() throws SQLException {
        if (commonParameterMap == null) {
            commonParameterMap = obtainCommonParameterMap();
        }
        return Maps.newHashMap(commonParameterMap);
    }

    /**
     * Returns common parameters of the table which can be used in the SQL CREATE TABLE statement. A possible list of
     * parameters defined in the OTHER_PARAMETER_MAP.
     * 
     * @return a map of the common table's parameters.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    private Map<String, String> obtainCommonParameterMap() throws SQLException {
        Map<String, String> parameters = Maps.newHashMap();
        Connection connection = null;
        ResultSet rs = null;
        Statement stmt = null;
        try {
            connection = dataSource.getConnection();
            stmt = connection.createStatement();
            rs = stmt.executeQuery("SHOW TABLE STATUS WHERE Name="
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
            if (rs != null) {
                rs.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return parameters;
    }

    /**
     * Returns the list of tables' primary keys.
     * 
     * @return A list of {@link TablePrimaryKey} object represented foreign keys.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    public List<TablePrimaryKey> getPrimaryKeyList() throws SQLException {
        // Lazy initialization
        if (primaryKeyList == null) {
            primaryKeyList = obtainPrimaryKeyList();
        }
        return Lists.newArrayList(primaryKeyList);
    }

    /**
     * Obtain from the database a list of tables' primary keys.
     * 
     * @return A list of {@link TablePrimaryKey} object represented foreign keys.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    private List<TablePrimaryKey> obtainPrimaryKeyList() throws SQLException {
        List<TablePrimaryKey> tablePrimaryKeyList = new ArrayList<TablePrimaryKey>();
        Connection connection = null;
        ResultSet primaryKeys = null;
        try {
            connection = dataSource.getConnection();
            DatabaseMetaData dbMetaData = connection.getMetaData();
            primaryKeys = dbMetaData.getPrimaryKeys(null, null, tableName);
            while (primaryKeys.next()) {
                if (primaryKeys.getString("PK_NAME") != null) {
                    tablePrimaryKeyList.add(new TablePrimaryKey(primaryKeys.getString("COLUMN_NAME")));
                }
            }
        } finally {
            if (primaryKeys != null) {
                primaryKeys.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return tablePrimaryKeyList;
    }

    /**
     * Returns the list of tables' foreign keys.
     * 
     * @return A list of {@link TablePrimaryKey} object represented foreign keys.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    public List<TableForeignKey> getForeignKeyList() throws SQLException {
        // Lazy initialization
        if (foreignKeyList == null) {
            foreignKeyList = obtainForeignKeyList();
        }
        return Lists.newArrayList(foreignKeyList);
    }

    /**
     * Obtains the list of tables foreign keys.
     * 
     * @return A list of {@link TableForeignKey} object represented foreign keys.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    private List<TableForeignKey> obtainForeignKeyList() throws SQLException {
        List<TableForeignKey> tableForeignKeyList = new ArrayList<TableForeignKey>();
        Connection connection = null;
        ResultSet foreignKeys = null;
        try {
            connection = dataSource.getConnection();
            DatabaseMetaData dbMetaData = connection.getMetaData();
            foreignKeys = dbMetaData.getImportedKeys(null, null, tableName);
            while (foreignKeys.next()) {
                if (foreignKeys.getString("FK_NAME") != null) {
                    tableForeignKeyList.add(new TableForeignKey(
                            foreignKeys.getString("FK_NAME"),
                            foreignKeys.getString("FKCOLUMN_NAME"),
                            foreignKeys.getString("PKTABLE_NAME"),
                            foreignKeys.getString("PKCOLUMN_NAME")));
                }
            }
        } finally {
            if (foreignKeys != null) {
                foreignKeys.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return tableForeignKeyList;
    }

    private final String tableName;
    private final DataSource dataSource;

    private Map<String, String> commonParameterMap;
    private List<TablePrimaryKey> primaryKeyList;
    private List<TableForeignKey> foreignKeyList;
    private List<TableColumn> tableStructureList;
    private List<TableRow> tableDataList;

    private static final Map<String, String> OTHER_PARAMETER_MAP = new HashMap<String, String>();
    static {
        OTHER_PARAMETER_MAP.put("Engine", "ENGINE");
        OTHER_PARAMETER_MAP.put("Collation", "COLLATE");
        OTHER_PARAMETER_MAP.put("Auto_increment", "AUTO_INCREMENT");
    }
}
