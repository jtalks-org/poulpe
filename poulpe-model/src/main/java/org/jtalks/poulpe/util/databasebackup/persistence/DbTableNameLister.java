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

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.Validate;
import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;

import com.google.common.collect.Lists;

/**
 * The class represents a list of tables which a given DataSource has.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class DbTableNameLister {
    /**
     * Constructs a new instance of the class with given DataSource.
     * 
     * @param dataSource
     *            a DataSource object for accessing the table.
     */
    public DbTableNameLister(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Returns currently set DataSource.
     * 
     * @return DataSource.
     */
    public DataSource getDataSource() {// TODO: check whether it's used anywhere
        return dataSource;
    }

    /**
     * Returns the list of all database table names which database contains from given Data source via JDBC.
     * 
     * @return a List of Strings where every String instance represents a table name from the database.
     * @throws SQLException
     *             is thrown if there is an error during collaborating with the database.
     */
    @SuppressWarnings("unchecked")
    public List<String> getTableNames() throws SQLException {// TODO: think about exceptions here, we might want to
                                                             // create our own exceptions
        Validate.notNull(dataSource, "dataSource must not be null");
        List<String> tableNames = null;
        try {
            tableNames = (List<String>) JdbcUtils.extractDatabaseMetaData(dataSource, new DatabaseMetaDataCallback() {
                @Override
                public Object processMetaData(final DatabaseMetaData dmd) throws SQLException, MetaDataAccessException {
                    final List<String> tableList = new ArrayList<String>(); // TODO: change using guava to direct
                                                                            // calling new ArrayList<String>()
                    ResultSet rs = null;
                    try {
                        rs = dmd.getTables(null, null, null, new String[] { "TABLE" });
                        while (rs.next()) {
                            tableList.add(rs.getString("TABLE_NAME"));
                        }
                    } finally {
                        if (rs != null) {// TODO: statement should close all realated ResultSets
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

    private final DataSource dataSource;
}
