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
package org.jtalks.poulpe.logic.databasebackup;

import java.io.InputStream;

/**
 * An abstract class which performs file downloading process by forcing a browser to start getting the file.
 *
 * @author Evgeny Surovtsev
 *
 */
public abstract class FileDownloader {
    /**
     * The method performs pushing input stream as a file to a browser's download process using predefined MIME Content
     * Type (see {@link #setMimeContentType(String)}) and Content Filename (see {@link #setContentFileName(String)}).
     *
     * @param content InputStream object which will be pushed to downloading.
     * @throws FileDownloadException throws and exception in case any errors occur during file downloading process.
     */
    protected abstract void download(InputStream content) throws FileDownloadException;

    /**
     * The method sets a MIME type for the content (Ex. "text/plain").
     *
     * @param mimeContentType String representation of the MIME content type.
     */
    public final void setMimeContentType(final String mimeContentType) {
        this.mimeContentType = mimeContentType;
    }

    /**
     * The method gets a MIME type for the content (Ex. "text/plain").
     *
     * @return A string representation of the MIME content type.
     */
    protected final String getMimeContentType() {
        return mimeContentType;
    }

    /**
     * Set the Content filename which will be used for storing content on the local disk for a user
     * (ex. "jtalks.sql").
     *
     * @param contentFileName The filename to set.
     */
    public final void setContentFileName(final String contentFileName) {
        this.contentFileName = contentFileName;
    }

    /**
     * Get the Content filename which will be used for storing content on the local disk for a user
     * (ex. "jtalks.sql").
     *
     * @return The filename to get.
     */
    protected final String getContentFileName() {
        return contentFileName;
    }

    // injected
    private String mimeContentType;
    // injected
    private String contentFileName;
}
