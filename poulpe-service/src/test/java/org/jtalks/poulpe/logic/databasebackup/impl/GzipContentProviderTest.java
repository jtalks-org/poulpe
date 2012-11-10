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
package org.jtalks.poulpe.logic.databasebackup.impl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.sql.DataSource;

import org.jtalks.poulpe.logic.databasebackup.exceptions.FileDownloadException;
import org.testng.annotations.Test;

/**
 * FileDownloadService.performFileDownload() method during its running should:
 * <ol>
 * <li>Get a content by calling ContentProvider.getContent();</li>
 * <li>Push browser to download prepared content by calling FileDownloader.download().</li>
 * </ol>
 * 
 * @author Evgeny Surovtsev
 */
public class GzipContentProviderTest {

    @Test
    public void contentProviderGzipsCorrectly() throws FileDownloadException, IOException {
        String expected = "Test string for checking GzipContentProvider class";

        DbDumpContentProvider contentProvider = mock(DbDumpContentProvider.class);
        when(contentProvider.getContentFileNameExt()).thenReturn(".sql");
        when(contentProvider.getContent()).thenReturn(new ByteArrayInputStream(expected.getBytes("UTF-8")));

        GzipContentProvider testObject = new GzipContentProvider(contentProvider);
        InputStream input = testObject.getContent();

        // BufferedReader in = new BufferedReader(new StringReader(expected));
        // ByteArrayOutputStream result = new ByteArrayOutputStream();
        // BufferedOutputStream out = new BufferedOutputStream(new GZIPOutputStream(result));
        // int c;
        // while ((c = in.read()) != -1) {
        // out.write(c);
        // }
        // in.close();
        // out.close();
        //
        // // read
        // InputStream input = new ByteArrayInputStream(result.toByteArray());

        // decompress
        BufferedReader in2 = new BufferedReader(new InputStreamReader(new GZIPInputStream(input)));
        String actual = in2.readLine();
        in2.close();

        assertEquals(actual, expected);
    }
}
