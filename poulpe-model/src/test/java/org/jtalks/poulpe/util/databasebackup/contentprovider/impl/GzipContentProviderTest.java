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

import static org.testng.Assert.*;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.jtalks.poulpe.util.databasebackup.contentprovider.ContentProvider;
import org.jtalks.poulpe.util.databasebackup.exceptions.FileDownloadException;
import org.jtalks.poulpe.util.databasebackup.exceptions.GzipPackingException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Tests that GZipContentProvider correctly performs gzipping on the giving content.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class GzipContentProviderTest {
    private GzipContentProvider sut;
    private ContentProvider dummyContentProvider;

    @BeforeMethod
    public void beforeMethod() throws Exception {
        dummyContentProvider = new DummyContentProvider(getContentExample());
        sut = new GzipContentProvider(dummyContentProvider);
    }

    @Test
    public void afterUncompressingMatchesOriginalString() throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        sut.writeContent(output);

        BufferedReader in2 = new BufferedReader(new InputStreamReader(new GZIPInputStream(
                new ByteArrayInputStream(output.toByteArray()))));
        String contentAfterUngzipping = in2.readLine();
        in2.close();

        assertEquals(contentAfterUngzipping, getContentExample());
    }

    @Test(expectedExceptions = GzipPackingException.class)
    public void IOErrorsWhenGzipThrowsException() throws IOException, FileDownloadException {
        GZIPOutputStream gzipOutput = mock(GZIPOutputStream.class);
        doThrow(IOException.class).when(gzipOutput).finish();

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        GzipContentProvider spySut = spy(sut);
        doReturn(gzipOutput).when(spySut).getGZIPOutputStream(output);

        spySut.writeContent(output);
    }

    @Test
    public void getMimeContentTypeTest() {
        assertEquals("application/x-gzip", sut.getMimeContentType());
    }

    @Test
    public void getContentFileNameExtTest() {
        assertEquals(".sql.gz", sut.getContentFileNameExt());
    }

    private String getContentExample() {
        return "Test string for checking GzipContentProvider class";
    }

    private static class DummyContentProvider implements ContentProvider {
        public DummyContentProvider(String contentExample) {
            this.contentExample = contentExample;
        }

        @Override
        public void writeContent(OutputStream output) throws FileDownloadException {
            try {
                output.write(contentExample.getBytes());

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

        private String contentExample;
    }
}
