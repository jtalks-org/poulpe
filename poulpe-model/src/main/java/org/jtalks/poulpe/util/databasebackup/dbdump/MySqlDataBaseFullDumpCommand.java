package org.jtalks.poulpe.util.databasebackup.dbdump;

import java.sql.SQLException;
import java.util.List;
import java.util.Queue;

import org.apache.commons.lang3.Validate;
import org.jtalks.poulpe.util.databasebackup.dbdump.mysqlsyntax.AddForeignKeysCommand;
import org.jtalks.poulpe.util.databasebackup.dbdump.mysqlsyntax.CommonHeaderCommand;
import org.jtalks.poulpe.util.databasebackup.dbdump.mysqlsyntax.CreateTableCommand;
import org.jtalks.poulpe.util.databasebackup.dbdump.mysqlsyntax.TableDataDumpCommand;
import org.jtalks.poulpe.util.databasebackup.persistence.DbTable;

import com.google.common.collect.Lists;

public class MySqlDataBaseFullDumpCommand implements DbDumpCommand {
    public MySqlDataBaseFullDumpCommand(final List<DbTable> dbTableList) {
        Validate.notNull(dbTableList, "dbTableList must not be null");
        dumpCommandQueue = Lists.newLinkedList();
        dumpCommandQueue.add(new CommonHeaderCommand());
        for (DbTable dbTable : dbTableList) {
            dumpCommandQueue.add(new CreateTableCommand(dbTable));
        }
        for (DbTable dbTable : dbTableList) {
            dumpCommandQueue.add(new TableDataDumpCommand(dbTable));
        }
        for (DbTable dbTable : dbTableList) {
            dumpCommandQueue.add(new AddForeignKeysCommand(dbTable));
        }
    }

    @Override
    public StringBuilder execute() throws SQLException {
        StringBuilder result = new StringBuilder();
        DbDumpCommand oneCommand = null;
        while ((oneCommand = dumpCommandQueue.poll()) != null) {
            result.append(oneCommand.execute());
        }

        return result;
    }

    private final Queue<DbDumpCommand> dumpCommandQueue;
}
