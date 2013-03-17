package org.jtalks.poulpe.util.databasebackup.contentprovider;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jtalks.poulpe.util.databasebackup.contentprovider.util.FileWrapper;
import org.jtalks.poulpe.util.databasebackup.exceptions.FileDownloadException;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * We need to test here that filename is in certain shape and that all methods for formating and downloading are called.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class FileDownloadServiceTest {
    @BeforeMethod
    public void beforeMethod() throws IOException {
        mockContentKeeper = Mockito.mock(ContentKeeper.class);
        when(mockContentKeeper.getContentFileNameExt()).thenReturn(".sql");
        when(mockContentKeeper.getMimeContentType()).thenReturn("MIME_TYPE");

        mockFileDownloader = Mockito.mock(FileDownloader.class);
        contentFileNameWithoutExt = "jtalks";

        mockFile = Mockito.mock(File.class);

        mockFileWrapper = Mockito.mock(FileWrapper.class);
        Mockito.when(mockFileWrapper.createTempFile(Mockito.anyString(), Mockito.anyString())).thenReturn(mockFile);

        mockOutputStream = Mockito.mock(OutputStream.class);

        mockInputStream = Mockito.mock(InputStream.class);

        sut = new FileDownloadService(mockContentKeeper, mockFileDownloader, contentFileNameWithoutExt) {
            // @Override
            // protected FileWrapper getFile() {
            // return mockFileWrapper;
            // }

            // @Override
            // protected OutputStream getFileOutputStream(final File contentFile) throws FileNotFoundException {
            // if (contentFile == mockFile) {
            // return mockOutputStream;
            // } else {
            // return null;
            // }
            // }

            // @Override
            // protected InputStream getFileInputStream(final File contentFile) throws FileNotFoundException {
            // if (contentFile == mockFile) {
            // return mockInputStream;
            // } else {
            // return null;
            // }
            // }
        };
    }

    /**
     * Test if formatted filename math certain timestamp format.
     */
    @Test
    public void filenameIsFormedWithTimestampAndBackupWord() {
        String actualFilename = sut.getContentFileNameWithoutExt();

        Assert.assertTrue(actualFilename.matches(getFileNameRegExp()), "Filename does not fit in the expected "
                + "format: YYYY-MM-DD_HH-MM-SS_jtalks_backup. Actual filename: " + actualFilename + ".");
    }

    @Test(enabled = false)
    public void performFileDownloadTest() throws Exception {
        // TODO: use private methods for constructing SUT and its DOCs
        String fullFileName = sut.getContentFileNameWithoutExt() + ".sql";

        sut.performFileDownload();

        // Mockito.verify(mockContentProvider).writeContent(mockOutputStream);
        Mockito.verify(mockOutputStream).close();
        // TODO: devide into 2 parts
        verify(mockFileDownloader).setMimeContentType("MIME_TYPE");
        verify(mockFileDownloader).setContentFileName(fullFileName);
        verify(mockFileDownloader).download(mockInputStream);
    }

    @Test(expectedExceptions = FileDownloadException.class, enabled = false)
    public void shouldRethrowExceptionIfIoErrorAppeared() throws Exception {
        Mockito.doThrow(IOException.class).when(mockOutputStream).close();
        sut.performFileDownload();
    }

    // @Test
    // public void getFileCachesReturnObject() {
    // sut = new FileDownloadService(mockContentProvider, mockFileDownloader, contentFileNameWithoutExt);
    // FileWrapper file1 = sut.fileWrapper();
    // FileWrapper file2 = sut.fileWrapper();
    //
    // Assert.assertTrue(file1 == file2);
    // }

    /**
     * Returns a regExp string for timestamp format: "YYYY-MM-DD_HH-MM-SS_jtalks_backup".
     * 
     * @return a regExp for timestamp
     */
    private String getFileNameRegExp() {
        return "^((19|20)\\d\\d)-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])_([01][0-9]|2[0-3])-"
                + "([0-5][0-9])-([0-5][0-9])_jtalks_backup$";
    }

    private FileDownloadService sut;
    private ContentKeeper mockContentKeeper;
    private FileDownloader mockFileDownloader;
    private String contentFileNameWithoutExt;
    private FileWrapper mockFileWrapper;
    private OutputStream mockOutputStream;
    private InputStream mockInputStream;
    private File mockFile;
}
