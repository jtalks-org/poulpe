package org.jtalks.poulpe.util.databasebackup.dbdump;

import org.jtalks.poulpe.util.databasebackup.exceptions.CreateDbDumpCommandException;

/**
 * Constructs a specific DbDumpCommand object. Each implementation provides different DbDump commands for different
 * database engines, different syntaxes. Also it's a responsibility of Factory to define what table will be put in the
 * dump by providing specific DbDumpCommand.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public interface DatabaseDumpFactory {
    /**
     * Returns an instance of {@link DbDumpCommand} which generates full database dump during its execution. Also it is
     * a responsibility of the method to define a list of tables for the dump and provide it to the newly created
     * DbDumpCommand.
     * 
     * @return a specific instance of DbDumpCommand for generating dump of database.
     * @throws CreateDbDumpCommandException
     *             if a error occurs.
     */
    DbDumpCommand newDbDumpCommand() throws CreateDbDumpCommandException;
}
