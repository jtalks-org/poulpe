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

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.lang3.Validate;
import org.jtalks.poulpe.util.databasebackup.contentprovider.ContentProvider;
import org.jtalks.poulpe.util.databasebackup.exceptions.DatabaseExportingException;
import org.jtalks.poulpe.util.databasebackup.exceptions.FileDownloadException;
import org.jtalks.poulpe.util.databasebackup.exceptions.GzipPackingException;

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
        File outFile;
        try {
            outFile = getFile().createTempFile("dbdump", getContentFileNameExt());
            gzipStream(contentProvider.getContent(), outFile);

        } catch (IOException e) {
            throw new GzipPackingException(e);
        }

        InputStream inputStream = null;
        try {
            inputStream = new DisposableFileInputStream(outFile);

        } catch (FileNotFoundException e) {
            throw new GzipPackingException(e);
        }
        return inputStream;
    }

    protected FileWrapper getFile() {
        if (fileWrapper == null) {
            fileWrapper = new FileWrapper();
        }
        return fileWrapper;
    }

    private FileWrapper fileWrapper;

    /**
     * Gzipping content of input File and saves it into output File.
     * 
     * @param inFile
     *            a file which content must be gzipped.
     * @param outFile
     *            a resulting file which contains gzipped content of inFile.
     * @throws IOException
     *             if any errors with input/output operations arrise.
     */
    private void gzipStream(InputStream input, File outFile) throws IOException {
        OutputStream output = null;

        try {
            output = new BufferedOutputStream(new GZIPOutputStream(getFileOutputStream(outFile)));
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = input.read(bytes)) != -1) {
                output.write(bytes, 0, read);
            }

        } finally {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.flush();
                output.close();
            }
        }
    }

    /**
     * Creates and returns a new OutputStream connected with given File. Method is open for overriding in tests for the
     * class.
     * 
     * @param outputFile
     *            a file to use for writing as an output point.
     * @return a newly created OutputStream.
     * @throws FileNotFoundException
     *             if provided File is not found.
     */
    protected OutputStream getFileOutputStream(File outputFile) throws FileNotFoundException {
        return new FileOutputStream(outputFile);
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
