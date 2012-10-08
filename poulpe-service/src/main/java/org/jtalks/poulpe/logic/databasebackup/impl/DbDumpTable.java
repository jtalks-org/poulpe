package org.jtalks.poulpe.logic.databasebackup.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class is used to export all information (structure, data) about concrete table from the database into text based
 * SQL commands shape.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class DbDumpTable {
    /**
     * The constructor creates a text based representation of the table with the same name as the name given in the
     * constructor's parameters. For that constructor successively calls Create Table Structure (see
     * {@link #getTableStructure()}) and Export data (see {@link #getDumpedData()}) methods.
     * 
     * @param connection
     *            an Instance of database Connection.
     * @param dbMetaData
     *            an Instance of DatabaseMetaData.
     * @param tableName
     *            the name of the table in the database which will be exported into text based form.
     * @throws SQLException
     *             is thrown when there is an SQL error during preparing the text based form of the table.
     */
    public DbDumpTable(final Connection connection, final DatabaseMetaData dbMetaData, final String tableName)
            throws SQLException {
        this.connection = connection;
        this.dbMetaData = dbMetaData;
        this.tableName = tableName;

        toStringRepresentation = (getTableStructure().append(getDumpedData())).toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return toStringRepresentation;
    }

    /**
     * Formats and return the table structure in the shape of SQL statements which can be run lately on SQL console for
     * the table creation.
     * 
     * @return A text based SQL statement for creating the table.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    private StringBuffer getTableStructure() throws SQLException {
        List<String> tableColumnsDescription = getTableColumnDescriptionList();
        List<String> primaryKeyList = getTablePrimaryKeyList();
        String otherTableParameters = getOtherTableParameters();

        return new StringBuffer(formatStructureHeader()).append(String.format(CREATE_STATEMENT,
                getSqlQuotedString(tableName), join(tableColumnsDescription, ",\n"), join(primaryKeyList, ","),
                otherTableParameters));
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
    private String getOtherTableParameters() throws SQLException {
        StringBuffer otherTableParameters = new StringBuffer();

        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(String.format(SHOW_TABLE_STATEMENT, getSqlQuotedString(tableName)));
            if (rs.next()) {
                for (String column : OTHER_PARAMETER_MAP.keySet()) {
                    String value = rs.getString(column);
                    if (value != null) {
                        otherTableParameters.append(OTHER_PARAMETER_MAP.get(column) + "=" + value + " ");
                    }
                }
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }

        return otherTableParameters.toString();
    }

    /**
     * Returns the list of the primary keys for the table.
     * 
     * @return List of the strings where each string contains a primary key's definition in the SQL terms and could
     *         attached to the CREATE TABLE statement lately.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    private List<String> getTablePrimaryKeyList() throws SQLException {
        List<String> tablePrimaryKeyList = new ArrayList<String>();

        ResultSet primaryKeys = dbMetaData.getPrimaryKeys(null, null, tableName);
        while (primaryKeys.next()) {
            String primaryKey = primaryKeys.getString("PK_NAME");
            if (primaryKey != null) {
                tablePrimaryKeyList.add(String.format(PRIMARY_KEY,
                        getSqlQuotedString(primaryKeys.getString("COLUMN_NAME"))));
            }
        }
        return tablePrimaryKeyList;
    }

    /**
     * Returns the list of table's columns description. For each column information about column's name, type and other
     * parameters returns. The information is provided in the form ready to be inserted into SQL CREATE TABLE statement.
     * 
     * @return A list of strings where each string represents description of one column.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    private List<String> getTableColumnDescriptionList() throws SQLException {
        List<String> tableColumnDescriptionList = new ArrayList<String>();
        ResultSet tableMetaData = null;
        try {
            tableMetaData = dbMetaData.getColumns(null, null, tableName, "%");

            while (tableMetaData.next()) {
                String nullString = ("NO".equalsIgnoreCase(tableMetaData.getString("IS_NULLABLE"))) ? "NOT NULL"
                        : "NULL";
                tableColumnDescriptionList.add(String.format(TABLE_COLUMN_DESCRIPTION,
                        getSqlQuotedString(tableMetaData.getString("COLUMN_NAME")),
                        tableMetaData.getString("TYPE_NAME"), tableMetaData.getInt("COLUMN_SIZE"), nullString));
            }
        } finally {
            if (tableMetaData != null) {
                tableMetaData.close();
            }
        }
        return tableColumnDescriptionList;
    }

    /**
     * Formats and return the table's actual data in the shape of SQL statements which can be run lately on SQL console
     * for inserting exported data into the table.
     * 
     * @return A text based SQL statements for inserting exported data into the table.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    private StringBuffer getDumpedData() throws SQLException {
        StringBuffer result = new StringBuffer(formatDumpedDataHeader());
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.prepareStatement(String.format(SELECT_STATEMENT, tableName));
            rs = stmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                List<String> nameColumns = new ArrayList<String>();
                List<String> valueColumns = new ArrayList<String>();
                for (int i = 0; i < columnCount; i++) {
                    Object value = rs.getObject(i + 1);
                    String columnValue = "NULL";
                    if (value != null) {
                        columnValue = getSqlQuotedString(value.toString());
                    }
                    nameColumns.add(getSqlQuotedString(metaData.getColumnName(i + 1)));
                    valueColumns.add(columnValue);
                }

                result.append(String.format(INSERT_STATEMENT, getSqlQuotedString(tableName), join(nameColumns, ","),
                        join(valueColumns, ",")));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
        }
        result.append("\n\n");
        return result;
    }

    // TODO: the method is very common and should be moved to an Utility class.
    /**
     * An utility's method which joins a given collection of Strings into one String using a given delimiter as a glue.
     * 
     * @param collection
     *            A collection of Strings to be joined.
     * @param delimiter
     *            A delimiter which will be used for joining the given Collection.
     * @return The String as a result of joining the given Collection with given delimiter.
     */
    private String join(final Collection<String> collection, final String delimiter) {
        if (collection.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String value : collection) {
            sb.append(value + delimiter);
        }
        sb.delete(sb.length() - delimiter.length(), sb.length());
        return sb.toString();
    }

    /**
     * The method prints a header for the table's exporting data.
     * 
     * @return A text formated header for the dump file.
     */
    private StringBuffer formatDumpedDataHeader() {
        StringBuffer dumpedDataHeader = new StringBuffer();
        dumpedDataHeader.append("--\n");
        dumpedDataHeader.append("-- Dumping data for table '" + tableName + "'\n");
        dumpedDataHeader.append("--\n\n");
        return dumpedDataHeader;
    }

    /**
     * The method prints a header for the table's exporting structure.
     * 
     * @return A text formated header for the dump file.
     */
    private StringBuffer formatStructureHeader() {
        StringBuffer structureHeader = new StringBuffer();
        structureHeader.append("--\n");
        structureHeader.append("-- Table structure for table '" + tableName + "'\n");
        structureHeader.append("--\n\n");
        return structureHeader;
    }

    /**
     * The method just quotes a given value, so it is available to use in the SQL statements.
     * 
     * @param value
     *            A value which will be quoted.
     * @return Already quoted value.
     */
    private String getSqlQuotedString(final String value) {
        String s = value;
        if (QUOTE_SIGN.length() > 0) {
            s = value.replaceAll(QUOTE_SIGN, "\\" + QUOTE_SIGN);
        }

        return QUOTE_SIGN + s + QUOTE_SIGN;
    }

    private final Connection connection;
    private final DatabaseMetaData dbMetaData;
    private final String tableName;

    private final String toStringRepresentation;

    private static final String QUOTE_SIGN = "'";
    private static final String INSERT_STATEMENT = "INSERT INTO %s (%s) VALUES (%s);\n";
    private static final String CREATE_STATEMENT = "CREATE TABLE IF NOT EXISTS %s (\n%s,\n%s\n) %s;\n\n";
    private static final String SELECT_STATEMENT = "SELECT * FROM %s";
    private static final String TABLE_COLUMN_DESCRIPTION = "    %s %s (%d) %s";
    private static final String PRIMARY_KEY = "    PRIMARY KEY (%s)";
    private static final String SHOW_TABLE_STATEMENT = "SHOW TABLE STATUS WHERE Name=%s";

    private static final Map<String, String> OTHER_PARAMETER_MAP = new HashMap<String, String>();
    static {
        OTHER_PARAMETER_MAP.put("Engine", "ENGINE");
        OTHER_PARAMETER_MAP.put("Collation", "COLLATE");
        OTHER_PARAMETER_MAP.put("Auto_increment", "AUTO_INCREMENT");
    }
}
