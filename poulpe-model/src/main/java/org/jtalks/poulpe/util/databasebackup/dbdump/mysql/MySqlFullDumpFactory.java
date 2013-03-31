package org.jtalks.poulpe.util.databasebackup.dbdump.mysql;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.jtalks.poulpe.util.databasebackup.dbdump.DatabaseDumpFactory;
import org.jtalks.poulpe.util.databasebackup.dbdump.DbDumpCommand;
import org.jtalks.poulpe.util.databasebackup.persistence.DbTable;
import org.jtalks.poulpe.util.databasebackup.persistence.DbTableLister;

public class MySqlFullDumpFactory implements DatabaseDumpFactory {
    public MySqlFullDumpFactory(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public DbDumpCommand newDbDumpCommand() throws SQLException {
        List<DbTable> tables = new DbTableLister(dataSource).getTables();
        return new MySqlDataBaseFullDumpCommand(tables);
    }

    private DataSource dataSource;
}
