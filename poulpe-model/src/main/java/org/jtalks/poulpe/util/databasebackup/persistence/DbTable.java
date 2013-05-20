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
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.commons.lang3.Validate;
import org.jtalks.poulpe.util.databasebackup.domain.ColumnMetaData;
import org.jtalks.poulpe.util.databasebackup.domain.ForeignKey;
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
     * @param processor
     *            a processor to perform some logic under every Row in the database.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    public void getData(RowProcessor processor) throws SQLException {
        dbTableData.getData(processor);
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
        return dbTableData.getStructure();
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
        return dbTableCommonParameters.getParameters();
    }

    /**
     * Returns the list of tables' primary keys.
     * 
     * @return A list of {@link UniqueKey} object represented foreign keys.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    public Set<UniqueKey> getPrimaryKeySet() throws SQLException {
        return dbTableKeys.getPrimaryKeys();
    }

    /**
     * Returns the list of tables' unique keys.
     * 
     * @return A list of {@link UniqueKey} object represented foreign keys.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    public Set<UniqueKey> getUniqueKeySet() throws SQLException {
        return dbTableKeys.getUniqueKeys();
    }

    /**
     * Returns the list of tables' foreign keys.
     * 
     * @return A list of {@link UniqueKey} object represented foreign keys.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    public Set<ForeignKey> getForeignKeySet() throws SQLException {
        return dbTableKeys.getForeignKeys();
    }

    private String tableName;

    private DbTableKeys dbTableKeys;
    private DbTableCommonParameters dbTableCommonParameters;
    private DbTableData dbTableData;
}
