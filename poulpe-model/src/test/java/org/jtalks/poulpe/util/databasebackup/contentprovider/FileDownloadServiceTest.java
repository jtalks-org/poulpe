package org.jtalks.poulpe.util.databasebackup.contentprovider;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * We need to test here that filename is in certain shape and that all methods for formating and downloading are called.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class FileDownloadServiceTest {
    private FileDownloadService sut;
    private ContentKeeper mockContentKeeper;
    private FileDownloader mockFileDownloader;

    @BeforeMethod
    public void beforeMethod() throws IOException {
        mockContentKeeper = mock(ContentKeeper.class);
        mockFileDownloader = mock(FileDownloader.class);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void doesntInitializeClassWithNullContentKeeper() {
        sut = new FileDownloadService(null, mockFileDownloader, "jtalks");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void doesntInitializeClassWithNullFileDownloader() {
        sut = new FileDownloadService(mockContentKeeper, null, "jtalks");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void doesntInitializeClassWithNullContentFileNameWithoutExt() {
        sut = new FileDownloadService(mockContentKeeper, mockFileDownloader, null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void doesntInitializeClassWithEmptyContentFileNameWithoutExt() {
        sut = new FileDownloadService(mockContentKeeper, mockFileDownloader, "");
    }

    @Test
    public void filenameIsFormedWithTimestampAndBackupWord() {
        sut = new FileDownloadService(mockContentKeeper, mockFileDownloader, "jtalks");
        String actualFilename = sut.getContentFileNameWithoutExt();

        assertTrue(actualFilename.matches(getFileNameRegExp()), "Filename does not fit in the expected "
                + "format: YYYY-MM-DD_HH-MM-SS_jtalks_backup. Actual filename: " + actualFilename + ".");
    }

    @Test
    public void performFileDownloadTest() throws Exception {
        InputStream mockInputStream = mock(InputStream.class);
        when(mockContentKeeper.getContentFileNameExt()).thenReturn(".sql");
        when(mockContentKeeper.getMimeContentType()).thenReturn("MIME_TYPE");
        when(mockContentKeeper.getInputStream()).thenReturn(mockInputStream);

        sut = new FileDownloadService(mockContentKeeper, mockFileDownloader, "jtalks");
        sut.performFileDownload();

        verify(mockFileDownloader).setMimeContentType("MIME_TYPE");
        verify(mockFileDownloader).setContentFileName(sut.getContentFileNameWithoutExt() + ".sql");
        verify(mockFileDownloader).download(mockInputStream);
    }

    /**
     * Returns a regExp string for timestamp format: "YYYY-MM-DD_HH-MM-SS_jtalks_backup".
     * 
     * @return a regExp for timestamp
     */
    private String getFileNameRegExp() {
        return "^((19|20)\\d\\d)-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])_([01][0-9]|2[0-3])-"
                + "([0-5][0-9])-([0-5][0-9])_jtalks_backup$";
    }
}
