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

import java.sql.SQLException;

public abstract class HeaderAndDataAwareCommand implements DbDumpCommand {

    @Override
    public final StringBuilder execute() throws SQLException {
        return new StringBuilder()
                .append(getHeader())
                .append(LINEFEED)
                .append(getData())
                .append(LINEFEED)
                .append(LINEFEED);
    }

    /**
     * Returns a description for the data which is provided by the command. The description will be used for formating
     * the whole result from the command executing.
     * 
     * @return text header for the provided data.
     */
    protected abstract StringBuilder getHeader();

    /**
     * Returns data, provided by the command. The date should dump the peace of information about table which is
     * provided (the peace of information) by the command.
     * 
     * @return provided data in the text shape.
     */
    protected abstract StringBuilder getData() throws SQLException;

    protected static final String LINEFEED = "\n";
}
