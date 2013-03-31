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

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jtalks.poulpe.util.databasebackup.dbdump.DatabaseDumpFactory;
import org.jtalks.poulpe.util.databasebackup.dbdump.DbDumpCommand;
import org.jtalks.poulpe.util.databasebackup.dbdump.mysql.MySqlDataBaseFullDumpCommand;
import org.jtalks.poulpe.util.databasebackup.exceptions.DatabaseExportingException;
import org.jtalks.poulpe.util.databasebackup.exceptions.FileDownloadException;
import org.jtalks.poulpe.util.databasebackup.persistence.DbTable;
import org.jtalks.poulpe.util.databasebackup.persistence.DbTableLister;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableList;

public class DbDumpContentProviderTest {
    private DbDumpContentProvider sut;
    private DatabaseDumpFactory mockDbDumpFactory;

    @BeforeMethod
    public void beforeMethod() throws SQLException {
        mockDbDumpFactory = mock(DatabaseDumpFactory.class);
        sut = new DbDumpContentProvider(mockDbDumpFactory);
    }

    @Test
    public void getMimeContentTypeTest() {
        assertEquals("text/plain", sut.getMimeContentType());
    }

    @Test
    public void getContentFileNameExtTest() {
        assertEquals(".sql", sut.getContentFileNameExt());
    }

    @Test
    public void writeContentTest() throws Exception {
        Pair<DbDumpCommand, OutputStream> pair = getDumpCommandAndOutput();
        DbDumpCommand command = pair.getLeft();
        OutputStream output = pair.getRight();

        sut.writeContent(output);
        verify(command).execute(output);
    }

    @Test(expectedExceptions = DatabaseExportingException.class)
    public void IOErrorsThrowException() throws Exception {
        Pair<DbDumpCommand, OutputStream> pair = getDumpCommandAndOutput();
        DbDumpCommand command = pair.getLeft();
        OutputStream output = pair.getRight();

        doThrow(IOException.class).when(command).execute(output);
        sut.writeContent(output);
    }

    @Test(expectedExceptions = DatabaseExportingException.class)
    public void SQLErrorsThrowException() throws Exception {
        Pair<DbDumpCommand, OutputStream> pair = getDumpCommandAndOutput();
        DbDumpCommand command = pair.getLeft();
        OutputStream output = pair.getRight();

        doThrow(SQLException.class).when(command).execute(output);
        sut.writeContent(output);
    }

    private Pair<DbDumpCommand, OutputStream> getDumpCommandAndOutput() throws Exception {
        OutputStream mockOutput = mock(OutputStream.class);
        DbDumpCommand mockDbDumpCommand = mock(DbDumpCommand.class);
        when(mockDbDumpFactory.newDbDumpCommand()).thenReturn(mockDbDumpCommand);

        return new ImmutablePair<DbDumpCommand, OutputStream>(mockDbDumpCommand, mockOutput);
    }
}
