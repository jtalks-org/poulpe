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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * When the dump file is generated it contains CREATE TABLE statements for each table from the database. Unfortunately
 * it's not possible to create tables (run CREATE TABLE statements) in free order because of relationships (via foreign
 * keys) between tables. So tables should be created in the order when table under creation is not dependent on tables
 * which are not created yet.
 * 
 * The main purpose of the class is to sort given table names list so they will be arranged in the order when a table in
 * the list is independent on the tables below. When this is ready we can export tables data from the top of the list to
 * its bottom.
 * 
 * @author Evgeny Surovtsev
 * 
 */
final class TableDependenciesResolver {
    /**
     * Constructor creates a new instance of Table Dependencies object for a givenTable Name.
     * 
     * @param tableName
     *            Specifies a Table Name which a Table Dependencies will be stored for.
     */
    private TableDependenciesResolver(final String tableName) {
        this.tableName = tableName;
    }

    /**
     * Returns Database table name for the current Dependency Table object.
     * 
     * @return Database Table Name
     */
    private String getTableName() {
        return tableName;
    }

    /**
     * Returns Dependencies (table names) for the current Table object.
     * 
     * @return Set of the table names which the current table is dependent on.
     */
    private Set<String> getDependencies() {
        return new HashSet<String>(dependencies);
    }

    /**
     * Adds a new table name which the current table is dependent on.
     * 
     * @param dependency
     *            Table name which the current table is dependent on.
     */
    private void addDependency(final String dependency) {
        dependencies.add(dependency);
    }

    /**
     * The static method is used to resolve dependencies for given list of table names (i.e. sorts table names so less
     * dependent tables will be on top and more dependent tables will be on the bottom).
     * 
     * @param tableDataInfoProvider
     *            An instance of service which provides information about table structure and table data (
     *            {@link TableDataInformationProvider}). The instance is used to get a list of foreign key for each
     *            table in the list.
     * @param tableNames
     *            A list of tables which should be sorted in order to resolve the dependencies.
     * @return A sorted list of table names where less dependent table are at a top of the list.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    public static List<String> resolveDependencies(final TableDataInformationProvider tableDataInfoProvider,
            final List<String> tableNames) throws SQLException {
        List<TableDependenciesResolver> tablesAndTheirDependencies = new ArrayList<TableDependenciesResolver>();
        for (String tableName : tableNames) {
            TableDependenciesResolver tableDependencies = new TableDependenciesResolver(tableName);
            List<TableDataForeignKey> foreignKeyList = tableDataInfoProvider.getForeignKeyList(tableName);
            for (TableDataForeignKey foreignKey : foreignKeyList) {
                tableDependencies.addDependency(foreignKey.getPkTableName());
            }
            tablesAndTheirDependencies.add(tableDependencies);
        }
        Collections.sort(tablesAndTheirDependencies, new Comparator<TableDependenciesResolver>() {
            @Override
            public int compare(final TableDependenciesResolver o1, final TableDependenciesResolver o2) {
                Set<String> o1Dependencies = o1.getDependencies();
                Set<String> o2Dependencies = o2.getDependencies();

                if (o1Dependencies.contains(o2.getTableName()) && o2Dependencies.contains(o1.getTableName())) {
                    // cross dependency - this should never happen!
                    return 0;
                } else if (o1Dependencies.contains(o2.getTableName())
                        || (o1Dependencies.size() > 0 && o2Dependencies.size() == 0)) {
                    return 1;
                } else if (o2Dependencies.contains(o1.getTableName())
                        || (o1Dependencies.size() == 0 && o2Dependencies.size() > 0)) {
                    return -1;
                }

                return 0;
            }
        });

        List<String> tables = new ArrayList<String>();
        for (TableDependenciesResolver dependencies : tablesAndTheirDependencies) {
            tables.add(dependencies.getTableName());
        }
        return tables;
    }

    private final String tableName;
    private final Set<String> dependencies = new HashSet<String>();
}
