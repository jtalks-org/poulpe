package org.jtalks.poulpe.util.databasebackup.exceptions;

import org.jtalks.poulpe.util.databasebackup.domain.Row;

/**
 * Is thrown if there is an Exceptional situation during Processing database {@link Row}.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class RowProcessingException extends Exception {
    /**
     * Instantiates a new instance with inner Exception.
     * 
     * @param e
     *            an inner Exception.
     */
    public RowProcessingException(Exception e) {
        super(e);
    }

    private static final long serialVersionUID = -279094377091702699L;
}
