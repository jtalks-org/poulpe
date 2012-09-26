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

import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
	
/**
 * @author Evgeny Surovtsev
 */
public class FileDownloadServiceTest {
	private FileDownloadService fileDownloadService;
	
	private ContentProvider contentProvider; 
	private FileDownloader fileDownloader;

	private static final String contentFileNameWithoutExt = "jtalks";
	private static final String mimeContentType = "plain/text";
	private static final String contentFileNameExt = ".sql";
	
    @BeforeMethod
    public void beforeMethod() throws FileDownloadException {
    	contentProvider = mock(ContentProvider.class);
    	doReturn(mimeContentType).when(contentProvider).getMimeContentType();
    	doReturn(contentFileNameExt).when(contentProvider).getContentFileNameExt();
    	
    	fileDownloader = mock(FileDownloader.class);
    	doNothing().when(fileDownloader).download(null);
    	doNothing().when(fileDownloader).setMimeContentType(mimeContentType);
    	doNothing().when(fileDownloader).setContentFileName(contentFileNameWithoutExt + contentFileNameExt);
    	
    	fileDownloadService = new FileDownloadService();
    	fileDownloadService.setContentFileNameWithoutExt(contentFileNameWithoutExt);
    	fileDownloadService.setContentProvider(contentProvider);
    	fileDownloadService.setFileDownloader(fileDownloader);
    }

    @Test(dataProvider = "getContentString")
    public void PerformFileDownloadTest(String contentString) 
    		throws FileDownloadException, UnsupportedEncodingException {
    	
    	InputStream content = new ByteArrayInputStream(contentString.getBytes("UTF-8"));
    	doReturn(content).when(contentProvider).getContent();
    	
    	fileDownloadService.performFileDownload();
    	
        verify(fileDownloader).setMimeContentType(mimeContentType);
        verify(fileDownloader).setContentFileName(contentFileNameWithoutExt + contentFileNameExt);
        verify(fileDownloader).download(content);
    }

    @DataProvider
    public Object[][] getContentString() {
        return new Object[][]{
                {"Test content"},
                {"1234567890"},
                {"Тестовый контент"},
        };
    }
}
