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

class TableDependenciesResolver {
    public TableDependenciesResolver() {
        this("");
    }

    public TableDependenciesResolver(final String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public Set<String> getDependencies() {
        return new HashSet<String>(dependencies);
    }

    public void addDependency(final String dependency) {
        dependencies.add(dependency);
    }

    public static List<String> resolveDependencies(final TableDataInformationProvider tableDataUtil,
            final List<String> tableNames) throws SQLException {
        List<TableDependenciesResolver> tablesAndTheirDependencies = new ArrayList<TableDependenciesResolver>();
        for (String tableName : tableNames) {
            TableDependenciesResolver tableDependencies = new TableDependenciesResolver(tableName);
            List<TableDataForeignKey> foreignKeyList = tableDataUtil.getForeignKeyList(tableName);
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
