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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;

import junit.framework.Assert;

import org.jtalks.poulpe.util.databasebackup.exceptions.FileDownloadException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests that GZipContentProvider correctly performs gzipping on the giving content.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class GzipContentProviderTest {
    @BeforeMethod
    public void beforeMethod() throws UnsupportedEncodingException, FileDownloadException {
        content = "Test string for checking GzipContentProvider class";

        DbDumpContentProvider contentProvider = mock(DbDumpContentProvider.class);
        when(contentProvider.getContentFileNameExt()).thenReturn(".sql");
        when(contentProvider.getContent()).thenReturn(new ByteArrayInputStream(content.getBytes("UTF-8")));

        sut = new GzipContentProvider(contentProvider);
    }

    /**
     * Passes a string as an input for DbContentProvider, gets the gzipped content from GzipContentProvider, unzips it
     * and checks that result is the same as initial String.
     * 
     * @throws FileDownloadException
     *             Must never happen
     * @throws IOException
     *             Must never happen
     */
    @Test
    public void contentProviderGzipsCorrectly() throws FileDownloadException, IOException {
        BufferedReader in2 = new BufferedReader(new InputStreamReader(new GZIPInputStream(sut.getContent())));
        String actual = in2.readLine();
        in2.close();

        assertEquals(actual, content);
    }

    @Test
    public void getMimeContentTypeTest() {
        Assert.assertEquals("application/x-gzip", sut.getMimeContentType());
    }

    @Test
    public void getContentFileNameExtTest() {
        Assert.assertEquals(".sql.gz", sut.getContentFileNameExt());
    }

    private GzipContentProvider sut;
    private String content;
}
