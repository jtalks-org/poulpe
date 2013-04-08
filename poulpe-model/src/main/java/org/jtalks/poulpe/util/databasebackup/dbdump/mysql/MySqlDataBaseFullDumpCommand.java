/**
 * Copyright (C) 2011  JTalks.org Team
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jtalks.poulpe.util.databasebackup.dbdump.mysql;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Queue;

import org.apache.commons.lang3.Validate;
import org.jtalks.poulpe.util.databasebackup.common.collection.Lists;
import org.jtalks.poulpe.util.databasebackup.dbdump.DbDumpCommand;
import org.jtalks.poulpe.util.databasebackup.persistence.DbTable;

/**
 * Full backup command which can pick MySQL data from selected tables and push it to {@link OutputStream}.
 */
public class MySqlDataBaseFullDumpCommand implements DbDumpCommand {

    /**
     * Construct required ordered list of commands.
     * 
     * @param dbTableList
     *            list of tables for full backup
     */
    public MySqlDataBaseFullDumpCommand(List<DbTable> dbTableList) {
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

    /**
     * Execute whole needed commands and push full backup into {@link OutputStream}.
     * 
     * @param output
     *            stream receiver
     */
    @Override
    public void execute(OutputStream output) throws SQLException, IOException {
        DbDumpCommand oneCommand = null;
        while ((oneCommand = dumpCommandQueue.poll()) != null) {
            oneCommand.execute(output);
        }
    }

    private final Queue<DbDumpCommand> dumpCommandQueue;
}
