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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import junit.framework.Assert;

import org.jtalks.poulpe.util.databasebackup.contentprovider.ContentProvider;
import org.jtalks.poulpe.util.databasebackup.exceptions.FileDownloadException;
import org.jtalks.poulpe.util.databasebackup.exceptions.GzipPackingException;
import org.mockito.Mockito;
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

        contentProvider = new ContentProvider() {

            @Override
            public void writeContent(OutputStream output) throws FileDownloadException {
                try {
                    output.write(content.getBytes());
                } catch (IOException e) {
                    throw new FileDownloadException();
                }
            }

            @Override
            public String getMimeContentType() {
                return "MIME_TYPE";
            }

            @Override
            public String getContentFileNameExt() {
                return ".sql";
            }

        };

        sut = new GzipContentProvider(contentProvider);
    }

    @Test
    public void contentProviderGzipsCorrectly() throws FileDownloadException, IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        sut.writeContent(output);

        BufferedReader in2 = new BufferedReader(new InputStreamReader(new GZIPInputStream(
                new ByteArrayInputStream(output.toByteArray()))));
        String actual = in2.readLine();
        in2.close();

        assertEquals(actual, content);
    }

    @Test(expectedExceptions = GzipPackingException.class)
    public void IOErrorsWhenGzipThrowsException() throws IOException, FileDownloadException {
        final GZIPOutputStream gzipOutput = Mockito.mock(GZIPOutputStream.class);
        Mockito.doThrow(IOException.class).when(gzipOutput).finish();

        sut = new GzipContentProvider(contentProvider) {
            @Override
            protected GZIPOutputStream getGZIPOutputStream(OutputStream output) throws IOException {
                return gzipOutput;
            }
        };

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        sut.writeContent(output);
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
    private ContentProvider contentProvider;
}
