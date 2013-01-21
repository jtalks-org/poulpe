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
package org.jtalks.poulpe.util.databasebackup.contentprovider.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.Validate;
import org.jtalks.poulpe.util.databasebackup.contentprovider.ContentProvider;
import org.jtalks.poulpe.util.databasebackup.dbdump.DbDumpCommand;
import org.jtalks.poulpe.util.databasebackup.dbdump.MySqlDataBaseFullDumpCommand;
import org.jtalks.poulpe.util.databasebackup.exceptions.DatabaseDoesntContainTablesException;
import org.jtalks.poulpe.util.databasebackup.exceptions.DatabaseExportingException;
import org.jtalks.poulpe.util.databasebackup.exceptions.EncodingToUtf8Exception;
import org.jtalks.poulpe.util.databasebackup.exceptions.FileDownloadException;
import org.jtalks.poulpe.util.databasebackup.persistence.DbTable;
import org.jtalks.poulpe.util.databasebackup.persistence.DbTableNameLister;

import com.google.common.collect.Lists;

/**
 * The class generates and provides a database dump for given data source in the shape of SQL commands which can be
 * executed in the SQL console later.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class DbDumpContentProvider implements ContentProvider {

    /**
     * A constructor injects a data source variable which later will be used to generate database dump.
     * 
     * @param dataSource
     *            A DataSource object points to the data base to export.
     * @throws NullPointerException
     *             If dataSource is null.
     */
    DbDumpContentProvider(final DataSource dataSource) {
        Validate.notNull(dataSource, "dataSource must not be null");
        this.dataSource = dataSource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getContent() throws FileDownloadException {
        StringBuilder result = new StringBuilder();

        try {
            List<String> tableNames = getDbTableNameLister().getPlainList();
            if (tableNames.size() == 0) {
                throw new DatabaseDoesntContainTablesException();
            }

            result.append(getDbDumpCommand(getDbTableList(tableNames)).execute());
        } catch (SQLException e) {
            throw new DatabaseExportingException(e);
        }
        InputStream inputStream = null;
        try {
            inputStream = new ByteArrayInputStream(result.toString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new EncodingToUtf8Exception(e);
        }
        return inputStream;
    }

    protected DbTableNameLister getDbTableNameLister() {
        return new DbTableNameLister(dataSource);
    }

    protected DbDumpCommand getDbDumpCommand(final List<DbTable> tablesToDump) {
        return new MySqlDataBaseFullDumpCommand(tablesToDump);
    }

    private List<DbTable> getDbTableList(final List<String> tableNames) {
        List<DbTable> dbTableList = Lists.newArrayList();
        for (String tableName : tableNames) {
            dbTableList.add(new DbTable(dataSource, tableName));
        }
        return dbTableList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMimeContentType() {
        return MIME_CONTENT_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getContentFileNameExt() {
        return FILE_NAME_EXT;
    }

    private final DataSource dataSource;

    private static final String MIME_CONTENT_TYPE = "text/plain";
    private static final String FILE_NAME_EXT = ".sql";
}
