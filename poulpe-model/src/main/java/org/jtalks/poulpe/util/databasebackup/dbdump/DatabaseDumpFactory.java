package org.jtalks.poulpe.util.databasebackup.dbdump;

import java.sql.SQLException;

import org.jtalks.poulpe.util.databasebackup.persistence.DbTableLister;

public interface DatabaseDumpFactory {
    // /**
    // * Returns a DbTableLister object which can enumerate all table names which database has. The method is marked as
    // * protected so it can be substitute in the unit test class.
    // *
    // * @return a DbTableNameLister object.
    // */
    // DbTableLister newDbTableLister();

    /**
     * Returns an instance of {@link DbDumpCommand} which generates full database dump during its execution. The method
     * is marked as protected so it can be substitute in the unit test class.
     * 
     * @param tablesToDump
     *            a list of {@link DbTable} objects which are used as a source of database dump information.
     * @return an instance of DbDumpCommand for generating dump of database.
     * @throws SQLException
     */
    DbDumpCommand newDbDumpCommand() throws SQLException;
}
