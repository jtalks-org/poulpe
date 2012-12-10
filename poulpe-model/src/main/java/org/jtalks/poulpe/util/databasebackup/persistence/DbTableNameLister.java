package org.jtalks.poulpe.util.databasebackup.persistence;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang3.Validate;
import org.jtalks.poulpe.util.databasebackup.domain.ForeignKey;
import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;

import com.google.common.collect.Lists;

/**
 * The class represents a list of tables which a database has. While providing the list of database tables class also
 * resolves tables dependencies problems which can occur when the dump file is being generated.
 * 
 * When the dump file is being generated it contains CREATE TABLE statements for each table from the database.
 * Unfortunately it's not possible to create tables (run CREATE TABLE statements) in free order because of relationships
 * (via foreign keys) between tables. So tables should be created in the order when table under creation is not
 * dependent on tables which are not created yet.
 * 
 * To resolve this the class before returning the list of database tables sorts it so table names will be arranged in
 * the order when a table in the list is independent on the tables below. When this is ready we can export tables data
 * from the top of the list to its bottom.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class DbTableNameLister {
    public DbTableNameLister(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * Returns the list of all database table names which database contains from given Data source via JDBC.
     * 
     * @param dataSource
     *            A data source which will be used to connect to the database.
     * @return a List of Strings where every String instance represents a table name from the database.
     * @throws SQLException
     *             is thrown if there is an error during collaborating with the database.
     * @throws NullPointerException
     *             If dataSource is null.
     */
    @SuppressWarnings("unchecked")
    public List<String> getPlainList() throws SQLException {
        Validate.notNull(dataSource, "dataSource must not be null");
        List<String> tableNames = null;
        try {
            tableNames = (List<String>) JdbcUtils.extractDatabaseMetaData(dataSource, new DatabaseMetaDataCallback() {
                @Override
                public Object processMetaData(final DatabaseMetaData dmd) throws SQLException, MetaDataAccessException {
                    final List<String> tableList = Lists.newArrayList();
                    ResultSet rs = null;
                    try {
                        rs = dmd.getTables(null, null, null, new String[] { "TABLE" });
                        while (rs.next()) {
                            tableList.add(rs.getString("TABLE_NAME"));
                        }
                    } finally {
                        if (rs != null) {
                            rs.close();
                        }
                    }
                    return tableList;
                }
            });
        } catch (final MetaDataAccessException e) {
            throw new SQLException(e);
        }

        return Collections.unmodifiableList(tableNames);
    }

    /**
     * The method is used to resolve dependencies for given list of table names (i.e. sorts table names so less
     * dependent tables will be on top and more dependent tables will be on the bottom).
     * 
     * @param dataSource
     *            A data source which will be used to connect to the database.
     * @return A sorted list of table names where less dependent table are at a top of the list.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     * @throws NullPointerException
     *             If dataSource is null.
     */
    public List<String> getIndependentList() throws SQLException {
        Validate.notNull(dataSource, "dataSource must not be null");
        final List<String> tableNames = Lists.newArrayList(getPlainList());
        Collections.sort(tableNames);

        final List<TableDependencies> tablesAndTheirDependencies = Lists.newArrayList();
        for (final String tableName : tableNames) {
            final TableDependencies tableDependencies = new TableDependencies(tableName);
            for (final ForeignKey foreignKey : new DbTable(dataSource, tableName).getForeignKeySet()) {
                tableDependencies.addDependency(foreignKey.getPkTableName());
            }
            tablesAndTheirDependencies.add(tableDependencies);
        }
        Collections.sort(tablesAndTheirDependencies, new Comparator<TableDependencies>() {
            @Override
            public int compare(final TableDependencies o1, final TableDependencies o2) {
                final Set<String> o1Dependencies = o1.getDependencies();
                final Set<String> o2Dependencies = o2.getDependencies();

                if (o1Dependencies.contains(o2.getTableName()) && o2Dependencies.contains(o1.getTableName())) {
                    assert false : "Tables " + o1.getTableName() + " and " + o2.getTableName()
                            + " have cross dependency to each other!";
                } else if (o1Dependencies.contains(o2.getTableName()) || o1Dependencies.size() > 0
                        && o2Dependencies.size() == 0) {
                    return 1;
                } else if (o2Dependencies.contains(o1.getTableName()) || o1Dependencies.size() == 0
                        && o2Dependencies.size() > 0) {
                    return -1;
                }

                return 0;
            }
        });

        final List<String> tables = new ArrayList<String>();
        for (final TableDependencies dependencies : tablesAndTheirDependencies) {
            tables.add(dependencies.getTableName());
        }
        return Collections.unmodifiableList(tables);
    }

    /**
     * The class is used as a container for keeping information about database table dependencies.
     * 
     * @author Evgeny Surovtsev
     * 
     */
    private static final class TableDependencies {
        /**
         * Constructor creates a new instance of Table Dependencies object for a givenTable Name.
         * 
         * @param tableName
         *            Specifies a Table Name which a Table Dependencies will be stored for.
         */
        TableDependencies(final String tableName) {
            assert tableName != null : "tableName must not be null";
            this.tableName = tableName;
        }

        /**
         * Returns Database table name for the current Dependency Table object.
         * 
         * @return Database Table Name
         */
        String getTableName() {
            return tableName;
        }

        /**
         * Returns Dependencies (table names) for the current Table object.
         * 
         * @return Set of the table names which the current table is dependent on.
         */
        Set<String> getDependencies() {
            return new HashSet<String>(dependencies);
        }

        /**
         * Adds a new table name which the current table is dependent on.
         * 
         * @param dependency
         *            Table name which the current table is dependent on.
         */
        void addDependency(final String dependency) {
            assert dependency != null : "dependency must not be null";
            dependencies.add(dependency);
        }

        private final String tableName;
        private final Set<String> dependencies = new HashSet<String>();
    }

    private final DataSource dataSource;
}
