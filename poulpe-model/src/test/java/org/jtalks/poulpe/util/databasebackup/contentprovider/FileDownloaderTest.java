package org.jtalks.poulpe.util.databasebackup.contentprovider;

import java.io.InputStream;

import org.jtalks.poulpe.util.databasebackup.exceptions.FileDownloadException;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

public class FileDownloaderTest {
    @BeforeMethod
    public void beforeMethod() {
        sut = new FileDownloader() {
            @Override
            protected void download(InputStream content) throws FileDownloadException {
            }
        };
    }

    @Test
    public void setsMimeContentType() {
        sut.setMimeContentType("MIME_CONTENT_TYPE");
        Assert.assertEquals(sut.getMimeContentType(), "MIME_CONTENT_TYPE");
    }

    @Test
    public void setsContentFileName() {
        sut.setContentFileName("CONTENT_FILENAME");
        Assert.assertEquals(sut.getContentFileName(), "CONTENT_FILENAME");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void doesntAllowSetNullMimeContentType() {
        sut.setMimeContentType(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void doesntAllowSetEmptyMimeContentType() {
        sut.setMimeContentType("");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void doesntAllowSetNullContentFileName() {
        sut.setContentFileName(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void doesntAllowSetEmptyContentFileName() {
        sut.setContentFileName("");
    }

    private FileDownloader sut;
}
