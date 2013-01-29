package org.jtalks.poulpe.util.databasebackup.contentprovider.util;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.mockito.Mockito;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

public class DisposableFileInputStreamTest {

    @BeforeMethod
    public void beforeMethod() throws IOException {
        tempFile = File.createTempFile("removeMe", null);
    }

    @Test
    public void removeTempFileAfterClosing() throws IOException {
        DisposableFileInputStream input = new DisposableFileInputStream(tempFile);
        input.close();

        Assert.assertFalse("TempFile must be removed", tempFile.exists());
    }

    @Test(expectedExceptions = IOException.class)
    public void exceptionWhenTempFileCannotBeDeleted() throws IOException {
        final File mockFile = Mockito.mock(File.class);
        Mockito.when(mockFile.delete()).thenReturn(false);

        DisposableFileInputStream input = new DisposableFileInputStream(tempFile) {
            @Override
            protected File getFile() {
                return mockFile;
            }
        };

        input.close();
    }

    @AfterMethod
    public void afterMethod() {
        tempFile.delete();
    }

    private File tempFile;
}
