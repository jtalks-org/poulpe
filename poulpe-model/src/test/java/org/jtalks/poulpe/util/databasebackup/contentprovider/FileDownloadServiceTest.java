package org.jtalks.poulpe.util.databasebackup.contentprovider;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStream;

import org.jtalks.poulpe.util.databasebackup.contentprovider.impl.DbDumpContentProvider;
import org.jtalks.poulpe.util.databasebackup.exceptions.FileDownloadException;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * We need to test here that filename is in certain shape and that all methods for formating and downloading are called.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class FileDownloadServiceTest {
    /**
     * Test if formatted filename math certain timestamp format.
     */
    @Test(groups = { "databasebackup" })
    public void filenameIsFormedWithTimestampAndBackupWord() {
        FileDownloadService testObject = new FileDownloadService();
        testObject.setContentFileNameWithoutExt("jtalks");
        String actualFilename = testObject.getContentFileNameWithoutExt();

        Assert.assertTrue(actualFilename.matches(getFileNameRegExp()), "Filename does not fit in the expected "
                + "format: YYYY-MM-DD_HH-MM-SS_jtalks_backup. Actual filename: " + actualFilename + ".");
    }

    /**
     * Tests that all methods were called during the download process.
     * 
     * @throws FileDownloadException
     *             Must never happen.
     */
    @Test(groups = { "databasebackup" })
    public void performFileDownloadTest() throws FileDownloadException {
        DbDumpContentProvider contentProvider = mock(DbDumpContentProvider.class);
        when(contentProvider.getContentFileNameExt()).thenReturn(".sql");
        when(contentProvider.getMimeContentType()).thenReturn("MIME_TYPE");

        FileDownloader fileDownloader = mock(FileDownloader.class);

        FileDownloadService testObject = new FileDownloadService();
        testObject.setContentProvider(contentProvider);
        testObject.setFileDownloader(fileDownloader);
        testObject.setContentFileNameWithoutExt("jtalks");
        String fullFileName = testObject.getContentFileNameWithoutExt() + ".sql";

        testObject.performFileDownload();

        verify(contentProvider).getContent();

        verify(contentProvider).getMimeContentType();
        verify(fileDownloader).setMimeContentType("MIME_TYPE");
        verify(fileDownloader).setContentFileName(fullFileName);
        verify(fileDownloader).download(Mockito.any(InputStream.class));
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
