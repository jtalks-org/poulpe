/**
 * Copyright (C) 2012  JTalks.org Team
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * The class provides given string as a content.
 * 
 * @author Evgeny Surovtsev
 *
 */
public class StringContentProvider implements ContentProvider {
    /**
     * @param content String which represents the content
     */
    StringContentProvider(final String content) {
        this.content = content;
    }
	
    @Override
    public InputStream getContent() throws FileDownloadException {
        InputStream contentInputStream = null; 
        try {
            contentInputStream = new ByteArrayInputStream(content.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new FileDownloadException(e);
        }
        return contentInputStream;
    }

    @Override
    public String getMimeContentType() {
        return MIME_CONTENT_TYPE;
    }

    @Override
    public String getContentFileNameExt() {
        return FILE_NAME_EXT;
    }

    private String content;
	
    private final static String MIME_CONTENT_TYPE = "text/plain";  
    private final static String FILE_NAME_EXT = ".sql";
}