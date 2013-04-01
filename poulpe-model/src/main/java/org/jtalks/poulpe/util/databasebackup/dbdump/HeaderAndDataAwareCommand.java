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

/**
 * Pack description and data, push to {@link OutputStream}. Resulted sequence is reversible and ready for unpacking
 */
public abstract class HeaderAndDataAwareCommand implements DbDumpCommand {

    /**
     * Pack description and data with delimiters and push it to {@link OutputStream}.
     */
    @Override
    public void execute(OutputStream output) throws SQLException, IOException {
        Writer writer = new PrintWriter(output);
        putHeader(writer);
        writer.write(LINEFEED);
        putData(writer);
        writer.write(LINEFEED);
        writer.write(LINEFEED);
        writer.flush();
    }

    /**
     * Writes a description for the data which is provided by the command into given writer. The description will be
     * used for formating the whole result from the command executing.
     * 
     * @param writer
     *            a Writer to write information into.
     * @throws IOException
     *             if an I/O error occurs.
     */
    protected abstract void putHeader(Writer writer) throws IOException;

    /**
     * Writes data, provided by the command into given writer. The date should dump the peace of information about table
     * which is provided (the peace of information) by the command.
     * 
     * @param writer
     *            a Writer to write information into.
     * @throws SQLException
     *             if an SQL error occurs.
     * @throws IOException
     *             if an I/O error occurs.
     */
    protected abstract void putData(Writer writer) throws SQLException, IOException;

    protected static final String LINEFEED = "\n";
}
