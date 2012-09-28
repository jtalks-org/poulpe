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
package org.jtalks.poulpe.logic.databasebackup;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
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
public class FileDownloadServiceTest {
    private FileDownloadService fileDownloadService;

    private ContentProvider contentProvider;
    private FileDownloader fileDownloader;

    private static final String CONTENT_FILENAME_WITHOUT_EXT = "jtalks";
    private static final String MIME_CONTENT_TYPE = "plain/text";
    private static final String CONTENT_FILENAME_EXT = ".sql";

    /**
     * The method sets up, configures and injects objects: ContentProvider and FileDownloader.
     *
     * @throws FileDownloadException is put here because of fileDownloader.download() method.
     */
    @BeforeMethod
    public final void beforeMethod() throws FileDownloadException {
        contentProvider = mock(ContentProvider.class);
        doReturn(MIME_CONTENT_TYPE).when(contentProvider).getMimeContentType();
        doReturn(CONTENT_FILENAME_EXT).when(contentProvider).getContentFileNameExt();

        fileDownloader = mock(FileDownloader.class);
        doNothing().when(fileDownloader).download(null);
        doNothing().when(fileDownloader).setMimeContentType(MIME_CONTENT_TYPE);
        doNothing().when(fileDownloader).setContentFileName(CONTENT_FILENAME_WITHOUT_EXT + CONTENT_FILENAME_EXT);

        fileDownloadService = new FileDownloadService();
        fileDownloadService.setContentFileNameWithoutExt(CONTENT_FILENAME_WITHOUT_EXT);
        fileDownloadService.setContentProvider(contentProvider);
        fileDownloadService.setFileDownloader(fileDownloader);
    }

    /**
     *
     * @param contentString A given piece of Content for test upon.
     * @throws FileDownloadException Is thrown if there is an exception during preparing content of sending it
     * to download.
     * @throws UnsupportedEncodingException Is thrown if there is an exceprion during encoding a content String into
     * UTF-8.
     */
    @Test(dataProvider = "getContentString")
    public final void performFileDownloadTest(final String contentString)
            throws FileDownloadException, UnsupportedEncodingException {

        InputStream content = new ByteArrayInputStream(contentString.getBytes("UTF-8"));
        doReturn(content).when(contentProvider).getContent();

        fileDownloadService.performFileDownload();

        verify(fileDownloader).setMimeContentType(MIME_CONTENT_TYPE);
        verify(fileDownloader).setContentFileName(CONTENT_FILENAME_WITHOUT_EXT + CONTENT_FILENAME_EXT);
        verify(fileDownloader).download(content);
    }

    /**
     * The method provides different variants of content in the shape of String.
     *
     * @return An instance of content.
     */
    @DataProvider
    public final Object[][] getContentString() {
        return new Object[][]{
                {"Test content"},
                {"1234567890"},
                {"Тестовый контент"},
        };
    }
}
