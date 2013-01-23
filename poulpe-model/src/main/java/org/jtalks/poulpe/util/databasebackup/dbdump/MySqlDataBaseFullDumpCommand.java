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
package org.jtalks.poulpe.util.databasebackup.dbdump;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
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
    public void execute(OutputStream output) throws SQLException, IOException {
        DbDumpCommand oneCommand = null;
        while ((oneCommand = dumpCommandQueue.poll()) != null) {
            oneCommand.execute(output);
        }
    }

    private final Queue<DbDumpCommand> dumpCommandQueue;
}
