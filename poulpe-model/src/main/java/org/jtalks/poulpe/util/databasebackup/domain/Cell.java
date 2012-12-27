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

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jtalks.poulpe.util.databasebackup.persistence.SqlTypes;

/**
 * A one cell in the table's row. Every table has a number of rows where each row contains a number of cells.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public final class Cell {
    /**
     * Constructs a new cell object with provided column meta information and data object.
     * 
     * @param columnMetaInfo
     *            A column's meta information.
     * @param cellData
     *            Cell's data.
     * @throws NullPointerException
     *             If columnMetaInfo is null.
     */
    public Cell(final ColumnMetaData columnMetaInfo, final Object cellData) {
        Validate.notNull(columnMetaInfo, "columnMetaInfo must not be null");
        this.columnMetaInfo = columnMetaInfo;
        this.cellData = cellData;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(31, 17).append(cellData).append(columnMetaInfo).toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        Cell other = (Cell) obj;
        return new EqualsBuilder()
                .append(this.cellData, other.cellData)
                .append(this.columnMetaInfo, other.columnMetaInfo)
                .build();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * Return the column's name.
     * 
     * @return Column's name
     */
    public String getColumnName() {
        return columnMetaInfo.getName();
    }

    /**
     * Returns the SQL type of the column.
     * 
     * @return SQL type of the column.
     */
    public SqlTypes getSqlType() {
        return columnMetaInfo.getType();
    }

    /**
     * Return the column's data object.
     * 
     * @return Column's data
     */
    public Object getColumnData() {
        return cellData;
    }

    private final ColumnMetaData columnMetaInfo;
    private final Object cellData;
}
