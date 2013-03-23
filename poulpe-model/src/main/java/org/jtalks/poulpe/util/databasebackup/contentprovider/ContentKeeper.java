package org.jtalks.poulpe.util.databasebackup.contentprovider;

import java.io.InputStream;

import org.jtalks.poulpe.util.databasebackup.exceptions.FileDownloadException;

/**
 * Responsible for keeping previously generated content (db dump) and providing InputStream for it. Also the class
 * provides a MIME content type and filename extension.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public interface ContentKeeper {
    /**
     * Provides keeping content.
     * 
     * @return input stream for keeping content.
     * @throws FileDownloadException
     *             if a error during preparing or providing previously stored content occurs.
     */
    InputStream getInputStream() throws FileDownloadException;

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
