package org.jtalks.poulpe.util.databasebackup.exceptions;

import java.io.IOException;

/**
 * Is thrown when a error during saving content (db dump) occurs.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class ContentPersistenceException extends FileDownloadException {
    /**
     * Initializes instance of the class with given nested exception.
     * 
     * @param e
     *            a nested exception.
     */
    public ContentPersistenceException(IOException e) {
        super(e);
    }

    private static final long serialVersionUID = 8594900579344760531L;
}
