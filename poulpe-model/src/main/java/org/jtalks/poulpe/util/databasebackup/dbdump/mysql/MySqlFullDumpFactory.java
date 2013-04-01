package org.jtalks.poulpe.util.databasebackup.dbdump.mysql;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.jtalks.poulpe.util.databasebackup.dbdump.DatabaseDumpFactory;
import org.jtalks.poulpe.util.databasebackup.dbdump.DbDumpCommand;
import org.jtalks.poulpe.util.databasebackup.exceptions.CreateDbDumpCommandException;
import org.jtalks.poulpe.util.databasebackup.persistence.DbTable;
import org.jtalks.poulpe.util.databasebackup.persistence.DbTableLister;

public class MySqlFullDumpFactory implements DatabaseDumpFactory {
    public MySqlFullDumpFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public DbDumpCommand newDbDumpCommand() throws CreateDbDumpCommandException {
        try {
            List<DbTable> tables = new DbTableLister(dataSource).getTables();
            return new MySqlDataBaseFullDumpCommand(tables);

        } catch (SQLException e) {
            throw new CreateDbDumpCommandException(e);
        }
    }

    private DataSource dataSource;
}
