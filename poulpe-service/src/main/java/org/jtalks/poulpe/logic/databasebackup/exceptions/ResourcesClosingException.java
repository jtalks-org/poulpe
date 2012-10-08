package org.jtalks.poulpe.logic.databasebackup.exceptions;

/**
 * The Exception is thrown when there is a error during closing database resources. Usually a previous SQL Exception can
 * lead to throwing the ResourcesClosingException.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class ResourcesClosingException extends FileDownloadException {

    /**
     * Constructor creates an instance and uses error message from given Exception.
     * 
     * @param e
     *            An exception which is used to construct FileDownloadException.
     */
    public ResourcesClosingException(final Exception e) {
        super(e);
    }

    private static final long serialVersionUID = 1L;

}
