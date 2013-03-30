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
package org.jtalks.poulpe.util.databasebackup.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a Table's row. Each row can contain many {@link Cell} objects.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public final class Row {
    /**
     * Adds a new column data to the current row.
     * 
     * @param columnMetaInfo
     *            a meta info of new cell to add.
     * @param cellData
     *            a data for new cell to add.
     * @return this
     */
    public Row addCell(ColumnMetaData columnMetaInfo, Object cellData) {
        Validate.notNull(columnMetaInfo, "columnMetaInfo must not be null");

        cells.put(columnMetaInfo, cellData);
        return this;
    }

    /**
     * Returns a count of cells in the current row.
     * 
     * @return Cell count
     */
    public int getCellCount() {
        return cells.size();
    }

    @Override
    public int hashCode() {
        final int initialOddNum = 31;
        final int multiplierOddNum = 17;
        return new HashCodeBuilder(initialOddNum, multiplierOddNum).append(cells).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Row other = (Row) obj;
        return new EqualsBuilder().append(this.cells, other.cells).build();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public Set<Entry<ColumnMetaData, Object>> getRowSet() {
        return cells.entrySet();
    }

    private final Map<ColumnMetaData, Object> cells = new HashMap<ColumnMetaData, Object>();
}
