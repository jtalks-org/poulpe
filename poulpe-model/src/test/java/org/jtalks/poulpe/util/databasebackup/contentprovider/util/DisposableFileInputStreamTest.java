package org.jtalks.poulpe.util.databasebackup.contentprovider.util;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

public class DisposableFileInputStreamTest {

    @BeforeMethod
    public void beforeMethod() {
    }

    @Test
    public void removeTempFileAfterClosing() throws IOException {
        File tempFile = File.createTempFile("removeMe", null);
        DisposableFileInputStream input = new DisposableFileInputStream(tempFile);
        Assert.assertTrue("TempFile must exist and be a file", tempFile.exists() && tempFile.isFile());
        input.close();
        Assert.assertFalse("TempFile must be removed", tempFile.exists());
    }

    @Test
    public void exceptionWhenTempFileCannotBeDeleted() throws IOException {
        File tempFile = File.createTempFile("removeMe", null);
        DisposableFileInputStream input1 = new DisposableFileInputStream(tempFile);
        DisposableFileInputStream input2 = new DisposableFileInputStream(tempFile);

        try {
            input1.close();
            Assert.fail(tempFile.getAbsolutePath() + " must be blocked.");
        } catch (IOException e) {
            // doing nothing
        }
        input2.close();
        Assert.assertFalse("TempFile must be removed", tempFile.exists());
    }
}
