/**
 * Copyright (C) 2011  JTalks.org Team
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
package org.jtalks.poulpe.util.databasebackup.contentprovider;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.apache.commons.lang3.Validate;
import org.jtalks.poulpe.util.databasebackup.contentprovider.util.DisposableFileInputStream;
import org.jtalks.poulpe.util.databasebackup.contentprovider.util.FileWrapper;
import org.jtalks.poulpe.util.databasebackup.exceptions.FileDownloadException;

/**
 * The class is used to download a file in the browser. For performing this the class uses next objects which should be
 * set via setters:
 * <ul>
 * <li>{@link ContentProvider} - the interface gets content for the downloading file and probably performs an additional
 * transformation under it like compressing it and other such activities.
 * <li>{@link FileDownloader} - the class is responsible for sending the file to the browser.</li>
 * </ul>
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class FileDownloadService {
    /**
     * Constructor which sets ContentProvider, FileDownloader and ContentFilenameWithoutExt via its parameters.
     * 
     * @param contentProvider
     *            An instance of ContentProvider.
     * @param fileDownloader
     *            An instance of FileDownloader.
     * @param contentFileNameWithoutExt
     *            Filename without extension which will be used for suggesting browser.
     */
    public FileDownloadService(final ContentProvider contentProvider, final FileDownloader fileDownloader,
            final String contentFileNameWithoutExt) {
        Validate.notNull(contentProvider, "contentProvider must not be null");
        Validate.notNull(fileDownloader, "fileDownloader must not be null");
        Validate.notNull(contentFileNameWithoutExt, "contentFileNameWithoutExt must not be null");
        this.contentProvider = contentProvider;
        this.fileDownloader = fileDownloader;
        this.contentFileNameWithoutExt = contentFileNameWithoutExt;
    }

    /**
     * This is the main method of the class. The method performs the actual file preparing using the provided
     * {@link ContentProvider} and sends the resulting file to the browser using the provided {@link FileDownloader}.
     * 
     * @throws FileDownloadException
     *             is thrown in case of any errors during file preparing or sending it to the browser.
     */
    public void performFileDownload() throws FileDownloadException {
        try {
            // TODO: if possible refactor class to have less responsibilities
            File contentFile = getFile().createTempFile("dbdump", contentProvider.getContentFileNameExt());
            OutputStream output = getFileOutputStream(contentFile);
            contentProvider.writeContent(output);
            output.flush();
            output.close();

            InputStream input = getFileInputStream(contentFile);
            fileDownloader.setMimeContentType(contentProvider.getMimeContentType());
            fileDownloader.setContentFileName(getContentFileNameWithoutExt() + contentProvider.getContentFileNameExt());
            fileDownloader.download(input);

        } catch (IOException e) {
            throw new FileDownloadException(e);
        }
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
    protected InputStream getFileInputStream(final File contentFile) throws FileNotFoundException {
        return new DisposableFileInputStream(contentFile);
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
    protected OutputStream getFileOutputStream(final File contentFile) throws FileNotFoundException {
        return new BufferedOutputStream(new FileOutputStream(contentFile));
    }

    /**
     * Return a FileWrapper object which is used for calling static methods on {@code java.io.File}. Method is marked as
     * protected so it can be substitute in unit tests.
     * 
     * @return an instance of FileWrapper.
     */
    protected FileWrapper getFile() {
        // TODO: make it thread-safe (e.g. make it private final and create in field declaration)
        if (fileWrapper == null) {
            fileWrapper = new FileWrapper();
        }
        return fileWrapper;
    }

    /**
     * Returns contentFileNameWithoutExt with current TimeStamp mark.
     * 
     * @return String which represents the filename without extension
     */
    protected String getContentFileNameWithoutExt() {
        assert contentFileNameWithoutExt != null : "contentFileNameWithoutExt must be defined";
        return new StringBuilder()
                .append(getCurrentTimeStamp()).append("_").append(contentFileNameWithoutExt).append("_backup")
                .toString();
    }

    /**
     * Formats current TimeStamp into format YYYY-MM-DD_HH-MM-SS and returns it.
     * 
     * @return String representation of formatted TimeStamp
     */
    private String getCurrentTimeStamp() {
        return String.format("%1$tY-%1$tm-%1$td_%1$tH-%1$tM-%1$tS", new Date());
    }

    private final ContentProvider contentProvider;
    private final FileDownloader fileDownloader;
    private final String contentFileNameWithoutExt;
    private FileWrapper fileWrapper;
}
