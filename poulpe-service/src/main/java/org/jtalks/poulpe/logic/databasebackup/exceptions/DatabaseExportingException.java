package org.jtalks.poulpe.logic.databasebackup.exceptions;

/**
 * The Exception is thrown when a error during exporting data from the database happens.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class DatabaseExportingException extends FileDownloadException {
    /**
     * Constructor creates an instance and uses error message from given Exception.
     * 
     * @param e
     *            An exception which is used to construct FileDownloadException.
     */
    public DatabaseExportingException(final Exception e) {
        super(e);
    }

    private static final long serialVersionUID = 1L;
}
