package org.jtalks.poulpe.util.databasebackup.contentprovider;

import static org.testng.Assert.*;

import java.io.InputStream;

import org.jtalks.poulpe.util.databasebackup.exceptions.FileDownloadException;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;

public class FileDownloaderTest {
    private FileDownloader sut;

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
        assertEquals(sut.getMimeContentType(), "MIME_CONTENT_TYPE");
    }

    @Test
    public void setsContentFileName() {
        sut.setContentFileName("CONTENT_FILENAME");
        assertEquals(sut.getContentFileName(), "CONTENT_FILENAME");
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
}
