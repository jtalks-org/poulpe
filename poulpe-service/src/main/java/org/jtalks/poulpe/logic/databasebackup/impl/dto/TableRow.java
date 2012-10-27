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
package org.jtalks.poulpe.logic.databasebackup.impl.dto;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * The class represent a single row data from the table. The class is immutable.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public final class TableRow {
    /**
     * Adds an information about a new column (name and value) into the container.
     * 
     * @param columnName
     *            The name of the new column.
     * @param columnValue
     *            The value of the new column.
     * @return The object itself.
     */
    public TableRow addColumn(final String columnName, final String columnValue) {
        dumpData.put(columnName, columnValue);
        return this;
    }

    /**
     * Return previously save information about one row.
     * 
     * @return A map representation of Column Name - Column Value pairs for the row.
     */
    public Map<String, String> getColumnsValueMap() {
        return Maps.newHashMap(dumpData);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 17;
        result = prime * result + ((dumpData == null) ? 0 : dumpData.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        return (this == obj) || (obj instanceof TableRow && dumpData.equals(((TableRow) obj).dumpData));
    }

    @Override
    public String toString() {
        return "TableRow [dumpData=" + dumpData + "]";
    }

    private final Map<String, String> dumpData = Maps.newHashMap();
}
