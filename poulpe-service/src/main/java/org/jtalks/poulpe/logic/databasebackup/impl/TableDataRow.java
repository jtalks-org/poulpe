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
package org.jtalks.poulpe.logic.databasebackup.impl;

import java.util.HashMap;
import java.util.Map;

/**
 * The class represent a single row data from the table. The class is immutable.
 * 
 * @author Evgeny Surovtsev
 * 
 */
final class TableDataRow {

    /**
     * Initiate an instance of the class (a data row) with a given row information.
     * 
     * @param dumpData
     *            A map of pairs Column Name - Column Data information for a one row.
     */
    public TableDataRow(final Map<String, String> dumpData) {
        this.dumpData = new HashMap<String, String>(dumpData);
    }

    /**
     * Return previously save information about one row.
     * 
     * @return A map representation of Column Name - Column Value pairs for the row.
     */
    public Map<String, String> getColumnsValueMap() {
        return new HashMap<String, String>(dumpData);
    }

    private final Map<String, String> dumpData;
}
