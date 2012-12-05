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
package org.jtalks.poulpe.util.databasebackup.logic.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.Validate;
import org.jtalks.poulpe.util.databasebackup.exceptions.DataBaseDoesntContainTablesException;
import org.jtalks.poulpe.util.databasebackup.exceptions.DatabaseExportingException;
import org.jtalks.poulpe.util.databasebackup.exceptions.EncodingToUtf8Exception;
import org.jtalks.poulpe.util.databasebackup.exceptions.FileDownloadException;
import org.jtalks.poulpe.util.databasebackup.logic.ContentProvider;
import org.jtalks.poulpe.util.databasebackup.persistence.DbTable;
import org.jtalks.poulpe.util.databasebackup.persistence.DbTableNameList;

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
        StringBuilder result = new StringBuilder(getHeaderInfo());

        try {
            List<String> tableNames = DbTableNameList.getIndependentList(getDataSource());
            if (tableNames.size() == 0) {
                throw new DataBaseDoesntContainTablesException();
            }

            for (String tableName : tableNames) {
                SqlTableDump tableDump = new SqlTableDump(new DbTable(getDataSource(), tableName));
                result.append(tableDump.getFullDump());
            }

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

    /**
     * The method prints a header for the whole exported data file.
     * 
     * @return A text formated header for the dump file.
     */
    private StringBuilder getHeaderInfo() {
        StringBuilder headerInfo = new StringBuilder();

        headerInfo.append("--\n");
        headerInfo.append("-- Copyright (C) 2011  JTalks.org Team\n");
        headerInfo.append("-- This library is free software; you can redistribute it and/or\n");
        headerInfo.append("-- modify it under the terms of the GNU Lesser General Public\n");
        headerInfo.append("-- License as published by the Free Software Foundation; either\n");
        headerInfo.append("-- version 2.1 of the License, or (at your option) any later version.\n");
        headerInfo.append("-- This library is distributed in the hope that it will be useful,\n");
        headerInfo.append("-- but WITHOUT ANY WARRANTY; without even the implied warranty of\n");
        headerInfo.append("-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU\n");
        headerInfo.append("-- Lesser General Public License for more details.\n");
        headerInfo.append("-- You should have received a copy of the GNU Lesser General Public\n");
        headerInfo.append("-- License along with this library; if not, write to the Free Software\n");
        headerInfo.append("-- Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA\n");
        headerInfo.append("--\n\n");

        headerInfo.append("-- JTalks SQL Dump\n");
        headerInfo.append("-- Generation Time: ");
        headerInfo.append(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()));
        headerInfo.append("\n\n");
        headerInfo.append("-- --------------------------------------------------------");
        headerInfo.append("\n\n");
        return headerInfo;
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

    /**
     * Getter for the data source instance.
     * 
     * @return A previously set DataSource instance.
     */
    private DataSource getDataSource() {
        return dataSource;
    }

    /**
     * Setter for data source instance.
     * 
     * @param dataSource
     *            a DataSource instance which will be used to work with database.
     * @throws NullPointerException
     *             If dataSource is null.
     */
    public void setDataSource(final DataSource dataSource) {
        Validate.notNull(dataSource, "dataSource must not be null");
        this.dataSource = dataSource;
    }

    private DataSource dataSource;

    private static final String MIME_CONTENT_TYPE = "text/plain";
    private static final String FILE_NAME_EXT = ".sql";
}
