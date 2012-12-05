package org.jtalks.poulpe.util.databasebackup.logic;

import org.testng.Assert;
import org.testng.annotations.Test;

public class FileDownloadServiceTest {
    @Test
    public void filenameIsFormedWithTimestampAndBackupWord() {
        FileDownloadService testObject = new FileDownloadService();
        testObject.setContentFileNameWithoutExt("jtalks");
        String actualFilename = testObject.getContentFileNameWithoutExt();

        Assert.assertTrue(actualFilename.matches(getFileNameRegExp()), "Filename does not fit in the expected "
                + "format: YYYY-MM-DD_HH-MM-SS_jtalks_backup. Actual filename: " + actualFilename + ".");
    }

    private String getFileNameRegExp() {
        return "^((19|20)\\d\\d)-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])_([01][0-9]|2[0-3])-"
                + "([0-5][0-9])-([0-5][0-9])_jtalks_backup$";
    }
}
