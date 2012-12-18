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

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.Validate;
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
     * By default constructor. ContentProvider, FileDownloader and ContentFilenameWithoutExt should be set via setters.
     */
    FileDownloadService() {
    }

    /**
     * Constructor which sets ContentProvider, FileDownloader and ContentFilenameWithoutExt via its parameters.
     * 
     * @param contentProvider
     *            An instance of ContentProvider.
     * @param fileDownloader
     *            An instance of FileDownloader.
     * @param contentFileNameWithoutExt
     *            Filename without extension which will be used for suggesting browser.
     * @throws NullPointerException
     *             If any of contentProvider or fileDownloader or contentFileNameWithoutExt is null.
     */
    FileDownloadService(final ContentProvider contentProvider, final FileDownloader fileDownloader,
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
     * @throws NullPointerException
     *             If any of contentProvider or fileDownloader or contentFileNameWithoutExt is not defined.
     */
    public final void performFileDownload() throws FileDownloadException {
        Validate.notNull(contentProvider, "contentProvider is not defined");
        Validate.notNull(fileDownloader, "fileDownloader is not defined");
        Validate.notNull(contentFileNameWithoutExt, "contentFileNameWithoutExt is not defined");
        try {
            InputStream content = contentProvider.getContent();

            fileDownloader.setMimeContentType(contentProvider.getMimeContentType());
            fileDownloader.setContentFileName(getContentFileNameWithoutExt() + contentProvider.getContentFileNameExt());
            fileDownloader.download(content);
        } catch (Exception e) {
            throw new FileDownloadException(e);
        }
    }

    /**
     * Injects a Content provider object which will be used for creating content which later will be send to a browser.
     * 
     * @param contentProvider
     *            An instance of a Content Provider
     * @throws NullPointerException
     *             If contentProvider is null.
     */
    public final void setContentProvider(final ContentProvider contentProvider) {
        Validate.notNull(contentProvider, "contentProvider must not be null");
        this.contentProvider = contentProvider;
    }

    /**
     * Injects a File Downloader object which "knows" how to send previously prepared content to a user's browser.
     * 
     * @param fileDownloader
     *            The instance of the FileDownloader
     * @throws NullPointerException
     *             If fileDownloader is null.
     */
    public final void setFileDownloader(final FileDownloader fileDownloader) {
        Validate.notNull(fileDownloader, "fileDownloader must not be null");
        this.fileDownloader = fileDownloader;
    }

    /**
     * Injects a local filename for prepared content in the shape without filename extension (Ex. "jtalks").
     * 
     * @param contentFileNameWithoutExt
     *            String which represents the filename without extension.
     * @throws NullPointerException
     *             If contentFileNameWithoutExt is null.
     */
    public final void setContentFileNameWithoutExt(final String contentFileNameWithoutExt) {
        Validate.notNull(contentFileNameWithoutExt, "contentFileNameWithoutExt must not be null");
        this.contentFileNameWithoutExt = contentFileNameWithoutExt;
    }

    /**
     * Returns contentFileNameWithoutExt with current TimeStamp mark.
     * 
     * @return String which represents the filename without extension
     */
    protected String getContentFileNameWithoutExt() {
        assert contentFileNameWithoutExt != null : "contentFileNameWithoutExt must be defined";
        return new StringBuilder()
                .append(getCurrentTimeStamp())
                .append("_")
                .append(contentFileNameWithoutExt)
                .append("_backup")
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

    // injected
    private ContentProvider contentProvider;
    // injected
    private FileDownloader fileDownloader;
    // injected
    private String contentFileNameWithoutExt;
}
