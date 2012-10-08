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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.jtalks.poulpe.logic.databasebackup.ContentProvider;
import org.jtalks.poulpe.logic.databasebackup.exceptions.DataBaseDoesntContainTablesException;
import org.jtalks.poulpe.logic.databasebackup.exceptions.DatabaseExportingException;
import org.jtalks.poulpe.logic.databasebackup.exceptions.EncodingToUtf8Exception;
import org.jtalks.poulpe.logic.databasebackup.exceptions.FileDownloadException;
import org.jtalks.poulpe.logic.databasebackup.exceptions.ResourcesClosingException;

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
     */
    DbDumpContentProvider(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getContent() throws FileDownloadException {
        Connection connection = null;
        StringBuffer result = new StringBuffer();
        result.append(getHeaderInfo());
        try {
            connection = getDataSource().getConnection();
            DatabaseMetaData dbMetaData = connection.getMetaData();

            List<String> tableNames = getTableNames(dbMetaData);
            if (tableNames.size() == 0) {
                throw new DataBaseDoesntContainTablesException();
            }

            for (String tableName : tableNames) {
                DbDumpTable table = new DbDumpTable(connection, dbMetaData, tableName);
                result.append(table);
            }
        } catch (SQLException e) {
            throw new DatabaseExportingException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new ResourcesClosingException(e);
                }
            }
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
     * Returns the list of all table names which database contains.
     * 
     * @param dbMetaData
     *            An object will be used to fetch information about table names database has.
     * @return a List of Strings where every String instance represents a table name from the database.
     * @throws SQLException
     *             is thrown if there is an error during calaborating with tha database.
     */
    private List<String> getTableNames(final DatabaseMetaData dbMetaData) throws SQLException {
        ResultSet tablesResultSet = null;
        List<String> tableNames = new ArrayList<String>();
        try {
            tablesResultSet = dbMetaData.getTables(null, null, null, null);
            while (tablesResultSet.next()) {
                if ("TABLE".equalsIgnoreCase(tablesResultSet.getString("TABLE_TYPE"))) {
                    tableNames.add(tablesResultSet.getString("TABLE_NAME"));
                }
            }
        } finally {
            if (tablesResultSet != null) {
                tablesResultSet.close();
            }
        }
        return tableNames;
    }

    /**
     * The method prints a header for the whole exported data file.
     * 
     * @return A text formated header for the dump file.
     */
    private StringBuffer getHeaderInfo() {
        StringBuffer headerInfo = new StringBuffer();
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
     */
    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private DataSource dataSource;

    private static final String MIME_CONTENT_TYPE = "text/plain";
    private static final String FILE_NAME_EXT = ".sql";
}
