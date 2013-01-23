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
package org.jtalks.poulpe.util.databasebackup.dbdump.mysqlsyntax;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jtalks.poulpe.util.databasebackup.dbdump.DbDumpCommand;

/**
 * The class prints a header for the whole exported data file.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class CommonHeaderCommand implements DbDumpCommand {

    @Override
    public void execute(OutputStream output) throws IOException {
        StringBuilder header = new StringBuilder();
        header.append("--").append(LINEFEED);
        header.append("-- Copyright (C) 2011  JTalks.org Team").append(LINEFEED);
        header.append("-- This library is free software; you can redistribute it and/or").append(LINEFEED);
        header.append("-- modify it under the terms of the GNU Lesser General Public").append(LINEFEED);
        header.append("-- License as published by the Free Software Foundation; either").append(LINEFEED);
        header.append("-- version 2.1 of the License, or (at your option) any later version.").append(LINEFEED);
        header.append("-- This library is distributed in the hope that it will be useful,").append(LINEFEED);
        header.append("-- but WITHOUT ANY WARRANTY; without even the implied warranty of").append(LINEFEED);
        header.append("-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU").append(LINEFEED);
        header.append("-- Lesser General Public License for more details.").append(LINEFEED);
        header.append("-- You should have received a copy of the GNU Lesser General Public").append(LINEFEED);
        header.append("-- License along with this library; if not, write to the Free Software").append(LINEFEED);
        header.append("-- Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA")
                .append(LINEFEED);
        header.append("--").append(LINEFEED).append(LINEFEED);

        header.append("-- JTalks SQL Dump").append(LINEFEED);
        header.append("-- Generation Time: ");
        header.append(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date()));
        header.append(LINEFEED).append(LINEFEED);
        header.append("-- --------------------------------------------------------");
        header.append(LINEFEED).append(LINEFEED);

        Writer writer = new PrintWriter(output);
        writer.write(header.toString());
        writer.flush();
    }

    private static final String LINEFEED = "\n";
}
