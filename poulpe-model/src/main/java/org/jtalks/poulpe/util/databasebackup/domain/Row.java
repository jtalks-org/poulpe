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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.common.collect.Maps;

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
    public Row addCell(final ColumnMetaData columnMetaInfo, final Object cellData) {
        Validate.notNull(columnMetaInfo, "columnMetaInfo must not be null");

        cells.put(columnMetaInfo, cellData);
        return this;
    }

    // /**
    // * Return previously saved information about one row.
    // *
    // * @return A List of Cells stored in the Row.
    // */
    // public List<Cell> getCellList() {
    // return Collections.unmodifiableList(cellList);
    // }

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
        return new HashCodeBuilder(31, 17).append(cells).toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Row other = (Row) obj;
        return new EqualsBuilder().append(this.cells, other.cells).build();
        // return this == obj || obj instanceof Row && areCellListsEqual((Row) obj);
    }

    // /**
    // * Checks if cell list for given Row object equals to cell list of instance itself. We cannot just compare two
    // Lists
    // * because if two lists have the same elements but in different order we still consider them as equal while with
    // * standard List.equals the order matters.
    // *
    // * @param obj
    // * an instance of Row which cellList will be compared to value of this
    // * @return true if cellLists are equal or false otherwise.
    // */
    // private boolean areCellListsEqual(final Row obj) {
    // return cellList.size() == obj.cellList.size() && cellList.containsAll(obj.cellList);
    // }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    private final Map<ColumnMetaData, Object> cells = Maps.newHashMap();

    public Set<Entry<ColumnMetaData, Object>> getRowSet() {
        return cells.entrySet();
    }
}
