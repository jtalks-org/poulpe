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
package org.jtalks.poulpe.util.databasebackup.contentprovider.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.lang3.Validate;
import org.jtalks.poulpe.util.databasebackup.contentprovider.ContentProvider;
import org.jtalks.poulpe.util.databasebackup.exceptions.FileDownloadException;
import org.jtalks.poulpe.util.databasebackup.exceptions.GzipPackingException;

/**
 * Decorates content provided by another content provider to compress it with GZIP. This ContentProvider is a filter
 * which copies and compresses (gzip) all content given by another ContentProvider.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class GzipContentProvider implements ContentProvider {

    /**
     * Construct GzipContentProvider instance with given ContentProvider-Source.
     * 
     * @param contentProvider
     *            All content provided by contentProvider will be gzipped and send further as a result of work of the
     *            GzipContentProvider.
     */
    public GzipContentProvider(ContentProvider contentProvider) {
        Validate.notNull(contentProvider, "contentProvider must not be null");
        this.contentProvider = contentProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeContent(OutputStream output) throws FileDownloadException {
        try {
            GZIPOutputStream gzipOutput = getGZIPOutputStream(output);
            contentProvider.writeContent(gzipOutput);
            gzipOutput.finish();
        } catch (IOException e) {
            throw new GzipPackingException(e);
        }
    }

    /**
     * Creates and returns a GZIP OutputStream which wraps given OutputStream.
     * 
     * @param output
     *            an OutputStream to wrap.
     * @return a newly created GZIPOutputStream.
     * @throws IOException
     *             if an I/O error has occurred.
     */
    protected GZIPOutputStream getGZIPOutputStream(OutputStream output) throws IOException {
        return new GZIPOutputStream(output);
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
