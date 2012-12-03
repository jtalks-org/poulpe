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
package org.jtalks.poulpe.util.databasebackup.logic;

import java.io.InputStream;

import org.jtalks.poulpe.util.databasebackup.exceptions.FileDownloadException;

/**
 * This is an interface for providing content which later will be shipped to a user as a file via standard browser's
 * download functionality. The interface defines common methods such as get a content or return MIME type or file based
 * extension for provided content.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public interface ContentProvider {
    /**
     * The "main" method which prepares content for downloading. Besides of actual preparing also a post processing
     * under the content like compressing it before sending to the browser and other such activities can be performed
     * here.
     * 
     * @return Content for sending to a user's browser as a file to download.
     * @throws FileDownloadException
     *             is thrown in case of any errors during content preparing.
     */
    InputStream getContent() throws FileDownloadException;

    /**
     * The method returns a MIME type for the content which was/will be provided by {@link #getContent()} method. This
     * method is used for setting MIME type in browser's response.
     * 
     * @return String which represents the defined MIME content type.
     */
    String getMimeContentType();

    /**
     * The method returns file extension for the content which was/will be provided by {@link #getContent()} method.
     * This needs for forming final filename which will be passed to the browser so browser will be adviced under which
     * name downloaded file should be saved.
     * 
     * @return Filename extension like ".html" with the dot prefixed.
     */
    String getContentFileNameExt();
}
