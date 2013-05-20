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

import org.apache.commons.lang.Validate;
import org.jtalks.poulpe.util.databasebackup.domain.ForeignKey;
import org.jtalks.poulpe.util.databasebackup.domain.TableKey;
import org.jtalks.poulpe.util.databasebackup.domain.UniqueKey;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The class is responsible for providing table's keys information.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class DbTableKeys {
    /**
     * Constructs a new instance of the class with given DataSource and TableName. These parameters will be used for
     * getting table's keys information.
     * 
     * @param dataSource
     *            a DataSource object for accessing the table.
     * @param tableName
     *            a name of the table to be dumped.
     */
    public DbTableKeys(DataSource dataSource, String tableName) {
        Validate.notNull(dataSource, "dataSource parameter must not be null.");
        Validate.notEmpty(tableName, "tableName parameter must not be empty.");
        this.dataSource = dataSource;
        this.tableName = tableName;
    }

    /**
     * Obtain from the database a list of tables' unique keys.
     * 
     * @return A list of {@link UniqueKey} object represented unique keys.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    @SuppressWarnings("unchecked")
    public Set<UniqueKey> getUniqueKeys() throws SQLException {
        Set<UniqueKey> tableUniqueKeySet = null;
        try {
            tableUniqueKeySet = (Set<UniqueKey>) JdbcUtils.extractDatabaseMetaData(dataSource, new KeyListProcessor(
                    tableName, new TableKeyPerformer() {
                        @Override
                        public ResultSet getResultSet(DatabaseMetaData dmd, String tableName)
                                throws SQLException {
                            return dmd.getIndexInfo(null, null, tableName, true, true);
                        }

                        @Override
                        public void addKeyToSet(ResultSet rs, Set<TableKey> keySet) throws SQLException {
                            if (rs.getString(INDEX_NAME) != null && rs.getString(COLUMN_NAME) != null) {
                                UniqueKey key =
                                        new UniqueKey(rs.getString(INDEX_NAME), rs.getString(COLUMN_NAME));
                                if (!isPrimaryKey(key)) {
                                    keySet.add(key);
                                }
                            }

                        }
                    }));

            Map<String, UniqueKey> resultMap = new HashMap<String, UniqueKey>();
            for (UniqueKey uniqueKey : tableUniqueKeySet) {
                if (resultMap.containsKey(uniqueKey.getIndexName())) {
                    Set<String> existingColumns = new HashSet<String>(resultMap.get(uniqueKey.getIndexName())
                            .getColumnNameSet());
                    existingColumns.addAll(uniqueKey.getColumnNameSet());
                    resultMap.put(uniqueKey.getIndexName(), new UniqueKey(uniqueKey.getIndexName(), existingColumns));
                } else {
                    resultMap.put(uniqueKey.getIndexName(), uniqueKey);
                }
            }
            tableUniqueKeySet.clear();
            for (UniqueKey key : resultMap.values()) {
                tableUniqueKeySet.add(key);
            }

        } catch (MetaDataAccessException e) {
            throw new SQLException(e);
        }
        return tableUniqueKeySet;
    }

    /**
     * Obtain from the database a list of tables' primary keys.
     * 
     * @return A list of {@link UniqueKey} object represented foreign keys.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    @SuppressWarnings("unchecked")
    public Set<UniqueKey> getPrimaryKeys() throws SQLException {
        if (primaryKeys != null) {
            return primaryKeys;
        }
        Set<UniqueKey> tablePrimaryKeySet = null;
        try {
            tablePrimaryKeySet = (Set<UniqueKey>) JdbcUtils.extractDatabaseMetaData(dataSource, new KeyListProcessor(
                    tableName, new TableKeyPerformer() {
                        @Override
                        public ResultSet getResultSet(DatabaseMetaData dmd, String tableName)
                                throws SQLException {
                            return dmd.getPrimaryKeys(null, null, tableName);
                        }

                        @Override
                        public void addKeyToSet(ResultSet rs, Set<TableKey> keySet) throws SQLException {
                            if (rs.getString(PK_NAME) != null && rs.getString(COLUMN_NAME) != null) {
                                keySet.add(new UniqueKey(rs.getString(PK_NAME), rs.getString(COLUMN_NAME)));
                            }

                        }
                    }));
        } catch (MetaDataAccessException e) {
            throw new SQLException(e);
        }
        primaryKeys = tablePrimaryKeySet;
        return tablePrimaryKeySet;
    }

    /**
     * Obtains the list of tables foreign keys.
     * 
     * @return A list of {@link ForeignKey} object represented foreign keys.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    @SuppressWarnings("unchecked")
    public Set<ForeignKey> getForeignKeys() throws SQLException {
        Set<ForeignKey> tableForeignKeySet = null;
        try {
            tableForeignKeySet = (Set<ForeignKey>) JdbcUtils.extractDatabaseMetaData(dataSource, new KeyListProcessor(
                    tableName, new TableKeyPerformer() {
                        @Override
                        public ResultSet getResultSet(DatabaseMetaData dmd, String tableName)
                                throws SQLException {
                            return dmd.getImportedKeys(null, null, tableName);
                        }

                        @Override
                        public void addKeyToSet(ResultSet rs, Set<TableKey> keySet) throws SQLException {
                            if (rs.getString(FK_NAME) != null) {
                                keySet.add(new ForeignKey(rs.getString(FK_NAME), rs.getString(FKCOLUMN_NAME), rs
                                        .getString(PKTABLE_NAME), rs.getString(PKCOLUMN_NAME)));
                            }

                        }
                    }));
        } catch (MetaDataAccessException e) {
            throw new SQLException(e);
        }
        return tableForeignKeySet;
    }

    /**
     * Checks if a given Unique key is a primary key. We need this functionality because primary key is an unique key as
     * the same time and there is no method to obtain list of unique key which are not primary keys. So for getting that
     * list we get a list of all unique keys (including primary) and then check every key in the list and remove it if
     * it is a primary ar the same time.
     * 
     * @param key
     *            an unique key to perform test upon.
     * @return true if given unique key is a primary at the same time or false otherwise.
     * @throws SQLException
     *             if any of errors occur during working with database.
     */
    private boolean isPrimaryKey(UniqueKey key) throws SQLException {
        // we need primaryKeyList loaded before we get list of unique keys.
        // because primary key is a unique key as well and we need a way to sort it out.
        if (primaryKeys == null) {
            primaryKeys = getPrimaryKeys();
        }

        for (UniqueKey primaryKey : primaryKeys) {
            if (primaryKey.getIndexName().equals(key.getIndexName())) {
                return true;
            }
        }
        return false;
    }

    private final String tableName;
    private final DataSource dataSource;

    private Set<UniqueKey> primaryKeys;

    private static final String COLUMN_NAME = "COLUMN_NAME";
    private static final String INDEX_NAME = "INDEX_NAME";
    private static final String PK_NAME = "PK_NAME";
    private static final String FK_NAME = "FK_NAME";
    private static final String FKCOLUMN_NAME = "FKCOLUMN_NAME";
    private static final String PKTABLE_NAME = "PKTABLE_NAME";
    private static final String PKCOLUMN_NAME = "PKCOLUMN_NAME";
}
