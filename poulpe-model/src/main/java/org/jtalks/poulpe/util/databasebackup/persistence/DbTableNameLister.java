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

    private final DataSource dataSource;
}
