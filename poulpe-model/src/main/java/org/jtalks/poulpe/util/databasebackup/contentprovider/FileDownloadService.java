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

import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang3.Validate;
import org.jtalks.poulpe.util.databasebackup.exceptions.FileDownloadException;

/**
 * The class is responsible for controlling the whole process of download file in the browser which includes: preparing
 * content, configuring parameters for the process of a file download and finally starting it.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class FileDownloadService {
    /**
     * Initializes an instance of the class with given parameters.
     * 
     * @param content
     *            is responsible for obtaining a content (db dump) and keeping it somewhere so FileDownloadService can
     *            access it later for sending back to the user.
     * @param fileDownloader
     *            is responsible for sending a previously prepared and persistent content back to the browser in the
     *            form of file for download.
     * @param contentFileNameWithoutExt
     *            Filename without extension which will be used for suggesting browser.
     */
    public FileDownloadService(ContentKeeper content, FileDownloader fileDownloader, String contentFileNameWithoutExt) {
        Validate.notNull(content, "content must not be null");
        Validate.notNull(fileDownloader, "fileDownloader must not be null");
        Validate.notNull(contentFileNameWithoutExt, "contentFileNameWithoutExt must not be null");
        this.content = content;
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
        fileDownloader.setMimeContentType(content.getMimeContentType());
        fileDownloader.setContentFileName(getContentFileNameWithoutExt() + content.getContentFileNameExt());
        fileDownloader.download(content.getInputStream());
    }

    /**
     * Returns contentFileNameWithoutExt with current TimeStamp mark.
     * 
     * @return String which represents the filename without extension
     */
    String getContentFileNameWithoutExt() {
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

    private final ContentKeeper content;
    private final FileDownloader fileDownloader;
    private final String contentFileNameWithoutExt;
}
