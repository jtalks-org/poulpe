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

import java.io.OutputStream;

import org.jtalks.poulpe.util.databasebackup.exceptions.FileDownloadException;

/**
 * This interface describes a way information about content must be provided for {@link FileDownloadService}.
 * FileDownloadService needs to know MIME type of the content, extension of a file the content can be saved under and
 * content data itself.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public interface ContentProvider {
    /**
     * Prepare or process content and save it into provided OutputStream.
     * 
     * @param output
     *            prepared content is stored into provided OutputStream.
     * @throws FileDownloadException
     *             if a error during content preparing occurs.
     */
    void writeContent(OutputStream output) throws FileDownloadException;

    /**
     * Return a MIME type for the content. The MIME type will be send to browser as a suggestion for type of downloading
     * file.
     * 
     * @return content's MIME type, such as "text/plain".
     */
    String getMimeContentType();

    /**
     * Returns filename's extension for the content. The provided extension is used for generating final filename which
     * will be passed to the browser as a suggestion of name file should be saved under.
     * 
     * @return Filename extension like ".html" with the dot prefixed.
     */
    String getContentFileNameExt();
}
