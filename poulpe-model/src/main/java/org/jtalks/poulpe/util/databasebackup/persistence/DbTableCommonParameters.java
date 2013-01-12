package org.jtalks.poulpe.util.databasebackup.persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.Validate;

import com.google.common.collect.Maps;

/**
 * The class is responsible for providing table's common parameters such as database engine, collation etc.
 * 
 * @author Evgeny Surovtsev
 * 
 */
class DbTableCommonParameters {
    /**
     * Constructs a new instance of the class with given DataSource and TableName. These parameters will be used for
     * preparing table's common parameters.
     * 
     * @param dataSource
     *            a DataSource object for accessing the table.
     * @param tableName
     *            a name of the table to be dumped.
     */
    public DbTableCommonParameters(final DataSource dataSource, final String tableName) {
        Validate.notNull(dataSource, "dataSource parameter mustnot be null.");
        Validate.notEmpty(tableName, "tableName parameter must not be empty.");
        this.dataSource = dataSource;
        this.tableName = tableName;
    }

    /**
     * Returns common parameters of the table which can be used in the SQL CREATE TABLE statement. A possible list of
     * parameters defined in the OTHER_PARAMETER_MAP.
     * 
     * @return a map of the common table's parameters.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    public Map<String, String> getParameters() throws SQLException {
        final Map<String, String> parameters = Maps.newHashMap();
        Connection connection = null;
        ResultSet rs = null;
        Statement stmt = null;
        try {
            connection = dataSource.getConnection();
            stmt = connection.createStatement();
            rs = stmt.executeQuery("SHOW TABLE STATUS WHERE Name=" + TableDataUtil.getSqlValueQuotedString(tableName));
            if (rs.next()) {
                for (final String column : OTHER_PARAMETER_MAP.keySet()) {
                    final String value = rs.getString(column);
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

    private static final Map<String, String> OTHER_PARAMETER_MAP = Maps.newHashMap();
    static {
        OTHER_PARAMETER_MAP.put("Engine", "ENGINE");
        OTHER_PARAMETER_MAP.put("Collation", "COLLATE");
        OTHER_PARAMETER_MAP.put("Auto_increment", "AUTO_INCREMENT");
    }

    private final String tableName;
    private final DataSource dataSource;
}
