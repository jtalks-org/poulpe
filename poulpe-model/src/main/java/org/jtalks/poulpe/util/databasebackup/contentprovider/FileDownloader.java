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

import org.apache.commons.lang3.Validate;
import org.jtalks.poulpe.util.databasebackup.exceptions.FileDownloadException;

/**
 * Performs file downloading in the browser with given content's data, MIME type and filename.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public abstract class FileDownloader {
    /**
     * Pushes input stream as a file to a browser's download process using predefined MIME Content Type (see
     * {@link #setMimeContentType(String)}) and Content Filename (see {@link #setContentFileName(String)}).
     * 
     * @param content
     *            InputStream object which will be pushed to downloading.
     * @throws FileDownloadException
     *             if a error occurs during file downloading process.
     */
    protected abstract void download(InputStream content) throws FileDownloadException;

    /**
     * Sets a MIME type for the content, such as "text/plain".
     * 
     * @param mimeContentType
     *            String representation of the MIME content type.
     */
    public void setMimeContentType(String mimeContentType) {
        Validate.notBlank(mimeContentType, "mimeContentType must be neither null nor blank");
        this.mimeContentType = mimeContentType;
    }

    /**
     * Gets a MIME type for the content.
     * 
     * @return A string representation of the MIME content type.
     */
    protected String getMimeContentType() {
        return mimeContentType;
    }

    /**
     * Sets the Content filename which will be suggested to browser as a filename for the downloaded file.
     * 
     * @param contentFileName
     *            The filename to set, such as "jtalks.sql".
     */
    public void setContentFileName(String contentFileName) {
        Validate.notBlank(contentFileName, "contentFileName must be neither null nor blank");
        this.contentFileName = contentFileName;
    }

    /**
     * Gets the Content filename.
     * 
     * @return The suggested filename of the content.
     */
    protected String getContentFileName() {
        return contentFileName;
    }

    private String mimeContentType;
    private String contentFileName;
}
