package org.jtalks.poulpe.util.databasebackup.persistence;

import org.jtalks.poulpe.util.databasebackup.domain.Row;
import org.jtalks.poulpe.util.databasebackup.exceptions.RowProcessingException;

/**
 * Responsible for performing some logic under given Row. Usually processor formats string representation of the given
 * row and pushes it into output stream.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public interface RowProcessor {
    /**
     * Process given Row.
     * 
     * @param rowToProcess
     *            a row to process.
     * @throws RowProcessingException
     *             if a error during processing occurs.
     */
    void process(Row rowToProcess) throws RowProcessingException;
}
