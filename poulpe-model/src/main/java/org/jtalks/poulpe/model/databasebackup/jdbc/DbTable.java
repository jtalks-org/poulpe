package org.jtalks.poulpe.model.databasebackup.jdbc;

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

import org.jtalks.poulpe.model.databasebackup.SqlTypes;
import org.jtalks.poulpe.model.databasebackup.dto.ColumnMetaData;
import org.jtalks.poulpe.model.databasebackup.dto.Cell;
import org.jtalks.poulpe.model.databasebackup.dto.ForeignKey;
import org.jtalks.poulpe.model.databasebackup.dto.TableKey;
import org.jtalks.poulpe.model.databasebackup.dto.UniqueKey;
import org.jtalks.poulpe.model.databasebackup.dto.Row;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;

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
     *            A dataSource to access to the database.
     * @param tableName
     *            A table name information about will be returned by the instance.
     * @throws NullPointerException
     *             if any of dataSource or tableName is null.
     */
    public DbTable(final DataSource dataSource, final String tableName) {
        if (dataSource == null) {
            throw new NullPointerException("dataSource parameter is null");
        }
        if (tableName == null) {
            throw new NullPointerException("tableName parameter is null");
        }
        this.tableName = tableName;
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * The method obtains and returns table rows data for the given table.
     * 
     * @return A list of obtained rows ({@link Row}).
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    public List<Row> getData() throws SQLException {
        if (tableDataList == null) {
            tableDataList = obtainTableDataList();
        }
        return tableDataList;
    }

    /**
     * Returns the list of table's columns description. For each column information about column's name, type and other
     * parameters returns. The information is provided in the form ready to be inserted into SQL CREATE TABLE statement.
     * 
     * @return A list of strings where each string represents description of one column.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    public List<ColumnMetaData> getStructure() throws SQLException {
        if (tableStructureList == null) {
            tableStructureList = obtainTableStructureList();
        }
        return Lists.newArrayList(tableStructureList);
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
     * Returns the list of tables' primary keys.
     * 
     * @return A list of {@link UniqueKey} object represented foreign keys.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    public List<UniqueKey> getPrimaryKeyList() throws SQLException {
        if (primaryKeyList == null) {
            primaryKeyList = obtainPrimaryKeyList();
        }
        return Lists.newArrayList(primaryKeyList);
    }

    /**
     * Returns the list of tables' unique keys.
     * 
     * @return A list of {@link UniqueKey} object represented foreign keys.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    public List<UniqueKey> getUniqueKeyList() throws SQLException {
        if (uniqueKeyList == null) {
            uniqueKeyList = obtainUniqueKeyList();
        }
        return Lists.newArrayList(uniqueKeyList);
    }

    /**
     * Returns the list of tables' foreign keys.
     * 
     * @return A list of {@link UniqueKey} object represented foreign keys.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    public List<ForeignKey> getForeignKeyList() throws SQLException {
        if (foreignKeyList == null) {
            foreignKeyList = obtainForeignKeyList();
        }
        return Lists.newArrayList(foreignKeyList);
    }

    /**
     * The method returns table's data in the shape of list of TableRow.
     * 
     * @return List of table rows.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    private List<Row> obtainTableDataList() throws SQLException {
        List<Row> dataDumpList = Collections.emptyList();
        try {
            dataDumpList = jdbcTemplate.query("select * from " + tableName,
                    new RowMapper<Row>() {
                        @Override
                        public Row mapRow(final ResultSet rs, final int rowNum) throws SQLException {
                            ResultSetMetaData metaData = rs.getMetaData();
                            int columnCount = metaData.getColumnCount();

                            Row row = new Row();
                            for (int i = 1; i <= columnCount; i++) {
                                row.addCell(new Cell(new ColumnMetaData(metaData.getColumnName(i), SqlTypes
                                        .getSqlTypeByJdbcSqlType(metaData.getColumnType(i))), rs.getObject(i)));
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
    private List<ColumnMetaData> obtainTableStructureList() throws SQLException {
        List<ColumnMetaData> tableColumnList = Lists.newArrayList();
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
            rs = stmt.executeQuery("SELECT * FROM " + tableName + " WHERE 1 = 0");
            rsmd = rs.getMetaData();
            int numberOfColumns = rsmd.getColumnCount();

            for (int i = 1; i <= numberOfColumns; i++) {
                tableColumnList.add(getColumnMetaData(rsmd, columnDefaultValues, i));
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
    private ColumnMetaData getColumnMetaData(final ResultSetMetaData rsmd,
            final Map<String, String> columnDefaultValues, final int i)
            throws SQLException {
        SqlTypes columnType = SqlTypes.getSqlTypeByJdbcSqlType(rsmd.getColumnType(i));

        ColumnMetaData column = new ColumnMetaData(rsmd.getColumnName(i), columnType)
                .setNullable(rsmd.isNullable(i) == ResultSetMetaData.columnNullable)
                .setAutoincrement(rsmd.isAutoIncrement(i));
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
        Map<String, String> columnDefaultValues = Maps.newHashMap();
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
     * Obtain from the database a list of tables' unique keys.
     * 
     * @return A list of {@link UniqueKey} object represented unique keys.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    @SuppressWarnings("unchecked")
    private List<UniqueKey> obtainUniqueKeyList() throws SQLException {
        // we need primaryKeyList loaded before we get list of unique keys.
        // because primary key is a unique key as well and we need a way to sort it out.
        if (primaryKeyList == null) {
            primaryKeyList = obtainPrimaryKeyList();
        }

        List<UniqueKey> tableUniqueKeyList = null;
        try {
            tableUniqueKeyList = (List<UniqueKey>) JdbcUtils.extractDatabaseMetaData(dataSource,
                    new KeyListProcessor(tableName, new TableKeyPerformer() {
                        @Override
                        public ResultSet getResultSet(final DatabaseMetaData dmd, final String tableName)
                                throws SQLException {
                            return dmd.getIndexInfo(null, null, tableName, true, true);
                        }

                        @Override
                        public void addKeyToList(final ResultSet rs, final List<TableKey> keyList) throws SQLException {
                            if (rs.getString("INDEX_NAME") != null
                                    && !primaryKeyList.contains(new UniqueKey(rs.getString("COLUMN_NAME")))) {
                                keyList.add(new UniqueKey(rs.getString("COLUMN_NAME")));
                            }

                        }
                    }));
        } catch (MetaDataAccessException e) {
            throw new SQLException(e);
        }
        return tableUniqueKeyList;
    }

    /**
     * Obtain from the database a list of tables' primary keys.
     * 
     * @return A list of {@link UniqueKey} object represented foreign keys.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    @SuppressWarnings("unchecked")
    private List<UniqueKey> obtainPrimaryKeyList() throws SQLException {
        List<UniqueKey> tablePrimaryKeyList = null;
        try {
            tablePrimaryKeyList = (List<UniqueKey>) JdbcUtils.extractDatabaseMetaData(dataSource,
                    new KeyListProcessor(tableName, new TableKeyPerformer() {
                        @Override
                        public ResultSet getResultSet(final DatabaseMetaData dmd, final String tableName)
                                throws SQLException {
                            return dmd.getPrimaryKeys(null, null, tableName);
                        }

                        @Override
                        public void addKeyToList(final ResultSet rs, final List<TableKey> keyList) throws SQLException {
                            if (rs.getString("PK_NAME") != null) {
                                keyList.add(new UniqueKey(rs.getString("COLUMN_NAME")));
                            }

                        }
                    }));
        } catch (MetaDataAccessException e) {
            throw new SQLException(e);
        }
        return tablePrimaryKeyList;
    }

    /**
     * Obtains the list of tables foreign keys.
     * 
     * @return A list of {@link ForeignKey} object represented foreign keys.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    @SuppressWarnings("unchecked")
    private List<ForeignKey> obtainForeignKeyList() throws SQLException {
        List<ForeignKey> tableForeignKeyList = null;
        try {
            tableForeignKeyList = (List<ForeignKey>) JdbcUtils.extractDatabaseMetaData(dataSource,
                    new KeyListProcessor(tableName, new TableKeyPerformer() {
                        @Override
                        public ResultSet getResultSet(final DatabaseMetaData dmd, final String tableName)
                                throws SQLException {
                            return dmd.getImportedKeys(null, null, tableName);
                        }

                        @Override
                        public void addKeyToList(final ResultSet rs, final List<TableKey> keyList) throws SQLException {
                            if (rs.getString("FK_NAME") != null) {
                                keyList.add(new ForeignKey(rs.getString("FK_NAME"), rs.getString("FKCOLUMN_NAME"),
                                        rs.getString("PKTABLE_NAME"), rs.getString("PKCOLUMN_NAME")));
                            }

                        }
                    }));
        } catch (MetaDataAccessException e) {
            throw new SQLException(e);
        }
        return tableForeignKeyList;
    }

    private final String tableName;
    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;

    private Map<String, String> commonParameterMap;
    private List<UniqueKey> primaryKeyList;
    private List<UniqueKey> uniqueKeyList;
    private List<ForeignKey> foreignKeyList;
    private List<ColumnMetaData> tableStructureList;
    private List<Row> tableDataList;

    private static final Map<String, String> OTHER_PARAMETER_MAP = new HashMap<String, String>();
    static {
        OTHER_PARAMETER_MAP.put("Engine", "ENGINE");
        OTHER_PARAMETER_MAP.put("Collation", "COLLATE");
        OTHER_PARAMETER_MAP.put("Auto_increment", "AUTO_INCREMENT");
    }

    /**
     * Class reads information about table keys for given table Set and creates a list of TableKeys. Class is used to
     * get rid of dublication while obtaining different types of keys (generally Primary, Foreign and Unique).
     * 
     * @author Evgeny Surovtsev
     * 
     */
    private static final class KeyListProcessor implements DatabaseMetaDataCallback {
        /**
         * Initializes KeyListProcessor with given table name and TableKeyPerformer.
         * 
         * @param tableName
         *            The name of the table.
         * @param tableKeyPerformer
         *            An instance of specific Key Performer (Strategy).
         */
        public KeyListProcessor(final String tableName, final TableKeyPerformer tableKeyPerformer) {
            super();
            assert (tableKeyPerformer != null);
            assert (tableName != null);
            this.tableKeyPerformer = tableKeyPerformer;
            this.tableName = tableName;
        }

        @Override
        public Object processMetaData(final DatabaseMetaData dmd) throws SQLException, MetaDataAccessException {
            List<TableKey> tableKeyList = Lists.newArrayList();
            ResultSet rs = tableKeyPerformer.getResultSet(dmd, tableName);
            while (rs.next()) {
                tableKeyPerformer.addKeyToList(rs, tableKeyList);
            }
            return tableKeyList;
        }

        private final TableKeyPerformer tableKeyPerformer;
        private final String tableName;
    }

    /**
     * Describes common interface for specific ways of table keys obtaining from ResultSets.
     * 
     * @author Evgeny Surovtsev
     * 
     */
    private interface TableKeyPerformer {
        /**
         * Returns result set which contains info about specific types of table keys. For example, ResultSet which
         * points to Foreign keys for the given table.
         * 
         * @param dmd
         *            An instance of DatabaseMetaData to obtain info from.
         * @param tableName
         *            The name of the table.
         * @return ResultSet which contains info about specific Table Keys.
         * @throws SQLException
         *             Is thrown in case any errors during work with database occur.
         */
        ResultSet getResultSet(DatabaseMetaData dmd, String tableName) throws SQLException;

        /**
         * Checks if current iterator of ResultSet contains info about specific table Key and add new instance of
         * TableKey to the given list, based on ResultSet under testing.
         * 
         * @param rs
         *            ResultSet which contains info about specific Table Keys.
         * @param keyList
         *            An existing List where an information about obtained Key will be put.
         * @throws SQLException
         *             Is thrown in case any errors during work with database occur.
         */
        void addKeyToList(ResultSet rs, List<TableKey> keyList) throws SQLException;
    }
}
