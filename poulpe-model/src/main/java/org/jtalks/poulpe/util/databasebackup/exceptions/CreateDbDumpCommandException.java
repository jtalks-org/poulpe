package org.jtalks.poulpe.util.databasebackup.exceptions;

/**
 * Is throws when there is a error while creating DbDumpCommand.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class CreateDbDumpCommandException extends Exception {
    /**
     * Initializes a new instance of exception with nested exception.
     * 
     * @param e
     *            nested exception.
     */
    public CreateDbDumpCommandException(Exception e) {
        super(e);
    }

    private static final long serialVersionUID = 360608478470047157L;
}
