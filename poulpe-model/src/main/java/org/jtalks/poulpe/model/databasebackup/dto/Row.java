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
package org.jtalks.poulpe.model.databasebackup.dto;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.Validate;

import com.google.common.collect.Lists;

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
     * @param cellData
     *            a new Cell object to add
     * @return this
     */
    public Row addCell(final Cell cellData) {
        Validate.notNull(cellData, "cellData must not be null");
        for (final Cell existColumnData : cellList) {
            if (existColumnData.getColumnName().equalsIgnoreCase(cellData.getColumnName())) {
                throw new IllegalArgumentException("TableColumnData " + cellData + " is already exist in the row "
                        + cellList);
            }
        }

        cellList.add(cellData);
        return this;
    }

    /**
     * Return previously saved information about one row.
     * 
     * @return A List of Cells stored in the Row.
     */
    public List<Cell> getCellList() {
        return Collections.unmodifiableList(cellList);
    }

    /**
     * Returns a count of cells in the current row.
     * 
     * @return Cell count
     */
    public int getCellCount() {
        return cellList.size();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 17;
        result = prime * result + (cellList == null ? 0 : cellList.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        return this == obj || obj instanceof Row && areCellListsEqual((Row) obj);
    }

    /**
     * Checks if cell list for given Row object equals to cell list of instance itself. We cannot just compare two Lists
     * because if two lists have the same elements but in different order we still consider them as equal while with
     * standard List.equals the order matters.
     * 
     * @param obj
     *            an instance of Row which cellList will be compared to value of this
     * @return true if cellLists are equal or false otherwise.
     */
    private boolean areCellListsEqual(final Row obj) {
        return cellList.size() == obj.cellList.size() && cellList.containsAll(obj.cellList);
    }

    @Override
    public String toString() {
        return "Row [cellList=" + cellList + "]";
    }

    private final List<Cell> cellList = Lists.newArrayList();
}
