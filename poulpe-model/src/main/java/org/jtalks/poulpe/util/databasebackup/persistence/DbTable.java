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

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang3.Validate;
import org.jtalks.poulpe.util.databasebackup.domain.ColumnMetaData;
import org.jtalks.poulpe.util.databasebackup.domain.ForeignKey;
import org.jtalks.poulpe.util.databasebackup.domain.Row;
import org.jtalks.poulpe.util.databasebackup.domain.UniqueKey;

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
    public DbTable(DataSource dataSource, String tableName) {
        Validate.notNull(dataSource, "dataSource must not be null");
        Validate.notNull(tableName, "tableName must not be null");
        this.tableName = tableName;

        this.dbTableKeys = new DbTableKeys(dataSource, tableName);
        this.dbTableCommonParameters = new DbTableCommonParameters(dataSource, tableName);
        this.dbTableData = new DbTableData(dataSource, tableName);
    }

    /**
     * Returns table name for the instance.
     * 
     * @return Table name
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * The method obtains and returns table rows data for the given table.
     * 
     * @return A list of obtained rows ({@link Row}).
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    public List<Row> getData() throws SQLException {
        if (tableData == null) {
            tableData = dbTableData.getData();
        }
        return Collections.unmodifiableList(tableData);
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
        if (tableStructure == null) {
            tableStructure = dbTableData.getStructure();
        }
        return Collections.unmodifiableList(tableStructure);
    }

    /**
     * Returns the map of additional table's parameters such as table's engine, collation, etc. Currently the checked
     * parameters are: Engine, Collation, Auto_increment.
     * 
     * @return A map of Parameter name - Parameter value pair for the given table.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    public Map<String, String> getCommonParameters() throws SQLException {
        if (commonParameters == null) {
            commonParameters = dbTableCommonParameters.getParameters();
        }
        return Collections.unmodifiableMap(commonParameters);
    }

    /**
     * Returns the list of tables' primary keys.
     * 
     * @return A list of {@link UniqueKey} object represented foreign keys.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    public Set<UniqueKey> getPrimaryKeySet() throws SQLException {
        if (primaryKeys == null) {
            primaryKeys = dbTableKeys.getPrimaryKeys();
        }
        return Collections.unmodifiableSet(primaryKeys);
    }

    /**
     * Returns the list of tables' unique keys.
     * 
     * @return A list of {@link UniqueKey} object represented foreign keys.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    public Set<UniqueKey> getUniqueKeySet() throws SQLException {
        if (uniqueKeys == null) {
            uniqueKeys = dbTableKeys.getUniqueKeys();
        }
        return Collections.unmodifiableSet(uniqueKeys);
    }

    /**
     * Returns the list of tables' foreign keys.
     * 
     * @return A list of {@link UniqueKey} object represented foreign keys.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    public Set<ForeignKey> getForeignKeySet() throws SQLException {
        if (foreignKeys == null) {
            foreignKeys = dbTableKeys.getForeignKeys();
        }
        return Collections.unmodifiableSet(foreignKeys);
    }

    private final String tableName;

    private Map<String, String> commonParameters;
    private Set<UniqueKey> primaryKeys;
    private Set<UniqueKey> uniqueKeys;
    private Set<ForeignKey> foreignKeys;
    private List<ColumnMetaData> tableStructure;
    private List<Row> tableData;

    private final DbTableKeys dbTableKeys;
    private final DbTableCommonParameters dbTableCommonParameters;
    private final DbTableData dbTableData;
}
