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
package org.jtalks.poulpe.util.databasebackup.persistence;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.Validate;
import org.jtalks.poulpe.util.databasebackup.domain.ColumnMetaData;
import org.jtalks.poulpe.util.databasebackup.domain.Row;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * The class is responsible for providing table structure and table data.
 * 
 * @author Evgeny Surovtsev
 * 
 */
class DbTableData {
    /**
     * Constructs a new instance of the class with given DataSource and TableName. These parameters will be used for
     * preparing table's structure and data.
     * 
     * @param dataSource
     *            a DataSource object for accessing the table.
     * @param tableName
     *            a name of the table to be dumped.
     */
    public DbTableData(DataSource dataSource, String tableName) {
        Validate.notNull(dataSource, "dataSource parameter must not be null.");
        Validate.notEmpty(tableName, "tableName parameter must not be empty.");
        this.dataSource = dataSource;
        this.tableName = tableName;

        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * The method returns table's data in the shape of list of TableRow.
     * 
     * @return List of table rows.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    public List<Row> getData() throws SQLException {
        List<Row> dataDumpList = Collections.emptyList();
        try {
            dataDumpList = jdbcTemplate.query(SELECT_FROM + tableName, new RowMapper<Row>() {
                @Override
                public Row mapRow(final ResultSet rs, final int rowNum) throws SQLException {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    Row row = new Row();
                    for (int i = 1; i <= columnCount; i++) {
                        ColumnMetaData columnMetaData = ColumnMetaData.getInstance(
                                metaData.getColumnName(i),
                                SqlTypes.getSqlTypeByJdbcSqlType(metaData.getColumnType(i)));
                        row.addCell(columnMetaData, rs.getObject(i));
                    }
                    return row;
                }
            });

        } catch (DataAccessException e) {
            throw new SQLException(e);
        }

        return dataDumpList;
    }

    /**
     * Returns the structure of the table in the shape of list of Table columns.
     * 
     * @return A list of Table column elements.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    public List<ColumnMetaData> getStructure() throws SQLException {
        List<ColumnMetaData> tableColumnList = new ArrayList<ColumnMetaData>();
        Statement stmt = null;
        ResultSet rs = null;
        ResultSetMetaData rsmd = null;
        Connection connection = null;
        try {
            // Get a list of defaults for the column
            // this cannot be done via ResultSetMetaData, so doing this via tableMetaData instead
            connection = dataSource.getConnection();
            DatabaseMetaData dbMetaData = connection.getMetaData();
            Map<String, String> columnDefaultValues = getColumnDefaults(dbMetaData);

            // Taking the rest of information from ResultSetMetaData object
            stmt = connection.createStatement();
            // WHERE 1 = 0 -- we don't need actual data, just a table structure, so lets make the query's result empty.
            rs = stmt.executeQuery(SELECT_FROM + tableName + " WHERE 1 = 0");
            rsmd = rs.getMetaData();
            int numberOfColumns = rsmd.getColumnCount();

            for (int i = 1; i <= numberOfColumns; i++) {
                tableColumnList.add(getColumnMetaData(rsmd, columnDefaultValues, i));
            }

        } finally {
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
     * Constructs a new ColumnMetaData objects from given ResultSetMetaData with provided column default values map and
     * the object's index.
     * 
     * @param rsmd
     *            A ResultSetMetaData which contains meta information about all columns for the table.
     * @param columnDefaultValues
     *            A map of possibly defined values by default for columns.
     * @param i
     *            Index of column which should be constructed.
     * @return A constructed ColumnMetaData object.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    private ColumnMetaData getColumnMetaData(ResultSetMetaData rsmd, Map<String, String> columnDefaultValues, int i)
            throws SQLException {
        SqlTypes columnType = SqlTypes.getSqlTypeByJdbcSqlType(rsmd.getColumnType(i));

        ColumnMetaData column = ColumnMetaData.getInstance(rsmd.getColumnName(i), columnType).setNullable(
                rsmd.isNullable(i) == ResultSetMetaData.columnNullable).setAutoincrement(rsmd.isAutoIncrement(i));
        if (columnDefaultValues.containsKey(rsmd.getColumnName(i))) {
            column.setDefaultValue(columnDefaultValues.get(rsmd.getColumnName(i)));
        }
        if (columnType.isHasSize()) {
            column.setSize(rsmd.getColumnDisplaySize(i));
        }
        return column;
    }

    /**
     * Gets a default values for each of the columns if that values are defined for the columns.
     * 
     * @param dbMetaData
     *            a DatabaseMetaData instance to fetch the information from.
     * @return A map where key is a Column name and value is Column's default.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    private Map<String, String> getColumnDefaults(final DatabaseMetaData dbMetaData) throws SQLException {
        Map<String, String> columnDefaultValues = new HashMap<String, String>();
        ResultSet tableMetaData = null;
        try {
            tableMetaData = dbMetaData.getColumns(null, null, tableName, "%");

            while (tableMetaData.next()) {
                String defaultValue = tableMetaData.getString("COLUMN_DEF");
                if (defaultValue != null && defaultValue.length() > 0) {
                    columnDefaultValues.put(tableMetaData.getString("COLUMN_NAME"), defaultValue);
                }
            }
        } finally {
            if (tableMetaData != null) {
                tableMetaData.close();
            }
        }
        return columnDefaultValues;
    }

    private final String tableName;
    private final DataSource dataSource;

    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_FROM = "SELECT * FROM ";
}
