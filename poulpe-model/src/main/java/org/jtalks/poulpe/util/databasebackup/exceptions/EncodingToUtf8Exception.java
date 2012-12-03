package org.jtalks.poulpe.util.databasebackup.exceptions;

/**
 * The Exception is thrown when a error happens during converted already exported data into UTF-8 before sending the
 * flow of data to the database backup functionality caller.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class EncodingToUtf8Exception extends FileDownloadException {

    /**
     * Constructor creates an instance and uses error message from given Exception.
     * 
     * @param e
     *            An exception which is used to construct FileDownloadException.
     */
    public EncodingToUtf8Exception(final Exception e) {
        super(e);
    }

    private static final long serialVersionUID = 1L;

}
