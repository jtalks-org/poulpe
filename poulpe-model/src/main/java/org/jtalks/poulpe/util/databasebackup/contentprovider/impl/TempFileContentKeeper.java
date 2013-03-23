package org.jtalks.poulpe.util.databasebackup.contentprovider.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.lang.Validate;
import org.jtalks.poulpe.util.databasebackup.contentprovider.ContentKeeper;
import org.jtalks.poulpe.util.databasebackup.contentprovider.ContentProvider;
import org.jtalks.poulpe.util.databasebackup.contentprovider.util.DisposableFileInputStream;
import org.jtalks.poulpe.util.databasebackup.contentprovider.util.FileWrapper;
import org.jtalks.poulpe.util.databasebackup.exceptions.ContentPersistenceException;
import org.jtalks.poulpe.util.databasebackup.exceptions.FileDownloadException;

/**
 * An implementation of {@link ContentKeeper} which uses temporary file for keeping content.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class TempFileContentKeeper implements ContentKeeper {
    /**
     * Initializes an instance of the class with given {@link ContentProvider}.
     * 
     * @param contentProvider
     *            An instance of ContentProvider. the interface gets content for the downloading file and probably
     *            performs an additional transformation under it like compressing it and other such activities.
     */
    public TempFileContentKeeper(ContentProvider contentProvider) {
        Validate.notNull(contentProvider);
        this.contentProvider = contentProvider;
    }

    @Override
    public InputStream getInputStream() throws FileDownloadException {
        try {
            File contentFile = fileWrapper().createTempFile("dbdump", contentProvider.getContentFileNameExt());
            OutputStream output = getFileOutputStream(contentFile);
            contentProvider.writeContent(output);
            output.flush();
            output.close();
            return getFileInputStream(contentFile);

        } catch (IOException e) {
            throw new ContentPersistenceException(e);
        }
    }

    @Override
    public String getMimeContentType() {
        return contentProvider.getMimeContentType();
    }

    @Override
    public String getContentFileNameExt() {
        return contentProvider.getContentFileNameExt();
    }

    /**
     * Return a FileWrapper object which is used for calling static methods on {@code java.io.File}. Method is marked as
     * protected so it can be substitute in unit tests.
     * 
     * @return an instance of FileWrapper.
     */
    FileWrapper fileWrapper() {
        return fileWrapper;
    }

    /**
     * Create and return new output stream based on given File.
     * 
     * @param contentFile
     *            the file to be opened for writing.
     * @return an output stream to write.
     * @throws FileNotFoundException
     *             if the file exists but is a directory rather than a regular file, does not exist but cannot be
     *             created, or cannot be opened for any other reason.
     */
    OutputStream getFileOutputStream(final File contentFile) throws FileNotFoundException {
        return new BufferedOutputStream(new FileOutputStream(contentFile));
    }

    /**
     * Create and return new input stream for a given File.
     * 
     * @param contentFile
     *            the file to be opened for reading.
     * @return an input stream to read.
     * @throws FileNotFoundException
     *             if the file does not exist, is a directory rather than a regular file, or for some other reason
     *             cannot be opened for reading.
     */
    InputStream getFileInputStream(final File contentFile) throws FileNotFoundException {
        return new DisposableFileInputStream(contentFile);
    }

    private final ContentProvider contentProvider;
    private final FileWrapper fileWrapper = new FileWrapper();
}
