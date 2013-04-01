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
import java.sql.SQLException;

/**
 * Interface for a TableDumpCommand. A command is supposed to be providing a piece of certain table's dump data, such as
 * table's CREATE statement, table's data, etc. So each command must override 2 methods:
 * <ul>
 * //TODO: move those methods description to HeaderAware..
 * <li><strong>{@link #getHeader()}</strong> - should return a descriptive header for the providing data.</li>
 * <li><strong>{@link #getData()}</strong> - should return a dumping data.</li>
 * </ul>
 * The information provided by the command will be packed and send to the content provider.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public interface DbDumpCommand {
    /**
     * Executes command and push result into {@link OutputStream}.
     * 
     * @param output
     *            stream receiver
     */
    void execute(OutputStream output) throws SQLException, IOException;
}
