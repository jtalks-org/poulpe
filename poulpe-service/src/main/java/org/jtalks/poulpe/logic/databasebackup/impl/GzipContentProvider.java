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
package org.jtalks.poulpe.logic.databasebackup.impl;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.lang3.Validate;
import org.jtalks.poulpe.logic.databasebackup.ContentProvider;
import org.jtalks.poulpe.logic.databasebackup.exceptions.DatabaseExportingException;
import org.jtalks.poulpe.logic.databasebackup.exceptions.FileDownloadException;

/**
 * Compresses (gzip) any content provided by given ContentProvider.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class GzipContentProvider implements ContentProvider {

    /**
     * Construct GzipContentProvider instance with given real content provider.
     * 
     * @param contentProvider
     *            A content, provided by contentProvider.getContent() will be gzipped and returned via
     *            GzipContentProvider.getContent().
     * @throws NullPointerException
     *             If contentProvider is null.
     */
    GzipContentProvider(final ContentProvider contentProvider) {
        Validate.notNull(contentProvider, "contentProvider must not be null");
        this.contentProvider = contentProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getContent() throws FileDownloadException {
        InputStream in = contentProvider.getContent();

        ByteArrayOutputStream result = null;
        BufferedOutputStream out = null;
        try {
            result = new ByteArrayOutputStream();
            out = new BufferedOutputStream(new GZIPOutputStream(result));
            int c;
            while ((c = in.read()) != -1) {
                out.write(c);
            }
            out.flush();
        } catch (IOException e) {
            throw new DatabaseExportingException(e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                throw new DatabaseExportingException(e);
            }
        }

        return new ByteArrayInputStream(result.toByteArray());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMimeContentType() {
        return MIME_CONTENT_TYPE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getContentFileNameExt() {
        return contentProvider.getContentFileNameExt() + FILE_NAME_EXT;
    }

    private final ContentProvider contentProvider;

    private static final String MIME_CONTENT_TYPE = "application/x-gzip";
    private static final String FILE_NAME_EXT = ".gz";
}
