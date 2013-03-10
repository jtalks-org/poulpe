/**
 * Copyright (C) 2012  JTalks.org Team
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

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import junit.framework.Assert;

import org.jtalks.poulpe.util.databasebackup.dbdump.DbDumpCommand;
import org.jtalks.poulpe.util.databasebackup.dbdump.MySqlDataBaseFullDumpCommand;
import org.jtalks.poulpe.util.databasebackup.exceptions.DatabaseDoesntContainTablesException;
import org.jtalks.poulpe.util.databasebackup.exceptions.DatabaseExportingException;
import org.jtalks.poulpe.util.databasebackup.exceptions.FileDownloadException;
import org.jtalks.poulpe.util.databasebackup.persistence.DbTable;
import org.jtalks.poulpe.util.databasebackup.persistence.DbTableNameLister;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableList;

public class DbDumpContentProviderTest {
    @BeforeMethod
    public void beforeMethod() throws SQLException {
        mockDataSource = Mockito.mock(DataSource.class);

        mockOutput = Mockito.mock(OutputStream.class);

        mockDbDumpCommand = Mockito.mock(DbDumpCommand.class);

        final List<String> tables = ImmutableList.of("tableName");
        mockDbTableNameLister = Mockito.mock(DbTableNameLister.class);
        Mockito.when(mockDbTableNameLister.getTableNames()).thenReturn(tables);

        sut = new DbDumpContentProvider(mockDataSource) {
            @Override
            protected DbDumpCommand getDbDumpCommand(final List<DbTable> tablesToDump) {
                return mockDbDumpCommand;
            }

            @Override
            protected DbTableNameLister getDbTableNameLister() {
                return mockDbTableNameLister;
            }
        };
    }

    @Test
    public void getMimeContentTypeTest() {
        Assert.assertEquals("text/plain", sut.getMimeContentType());
    }

    @Test
    public void getContentFileNameExtTest() {
        Assert.assertEquals(".sql", sut.getContentFileNameExt());
    }

    @Test
    public void checkDbDumpCommandType() {
        DbTable mockDbTable = Mockito.mock(DbTable.class);
        List<DbTable> tablesToDump = ImmutableList.of(mockDbTable);

        sut = new DbDumpContentProvider(mockDataSource);
        Assert.assertTrue(sut.getDbDumpCommand(tablesToDump) instanceof MySqlDataBaseFullDumpCommand);
    }

    @Test
    public void writeContentTest() throws FileDownloadException, SQLException, IOException {
        sut.writeContent(mockOutput);
        Mockito.verify(mockDbDumpCommand).execute(mockOutput);
    }

    @Test(expectedExceptions = DatabaseDoesntContainTablesException.class)
    public void emptyDatabaseThrowsException() throws SQLException, FileDownloadException {
        Mockito.when(mockDbTableNameLister.getTableNames()).thenReturn(new ArrayList<String>());
        sut.writeContent(mockOutput);
    }

    @Test(expectedExceptions = DatabaseExportingException.class)
    public void IOErrorsThrowException() throws SQLException, IOException, FileDownloadException {
        Mockito.doThrow(IOException.class).when(mockDbDumpCommand).execute(mockOutput);
        sut.writeContent(mockOutput);
    }

    @Test(expectedExceptions = DatabaseExportingException.class)
    public void SQLErrorsThrowException() throws SQLException, IOException, FileDownloadException {
        Mockito.doThrow(SQLException.class).when(mockDbDumpCommand).execute(mockOutput);
        sut.writeContent(mockOutput);
    }

    private DbDumpContentProvider sut;
    private DataSource mockDataSource;
    private OutputStream mockOutput;
    private DbDumpCommand mockDbDumpCommand;
    private DbTableNameLister mockDbTableNameLister;
}
