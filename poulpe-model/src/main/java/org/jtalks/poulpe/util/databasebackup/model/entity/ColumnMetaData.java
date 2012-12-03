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
package org.jtalks.poulpe.util.databasebackup.model.entity;

import org.apache.commons.lang3.Validate;
import org.jtalks.poulpe.util.databasebackup.model.jdbc.SqlTypes;

/**
 * The class describes structure of one table's Column. So every table has a number of columns and each column is
 * described by a particular ColumnMetaData object.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public final class ColumnMetaData {
    /**
     * Constructs a new Column meta data object with two given obligatory parameters: table column's name and type.
     * Other parameters should be set via setters.
     * 
     * @param columnName
     *            The name of the table column.
     * @param columnType
     *            The type of the table column.
     */
    public ColumnMetaData(final String columnName, final SqlTypes columnType) {
        Validate.notNull(columnName, "columnName must not be null");
        Validate.notNull(columnType, "columnType must not be null");
        this.name = columnName;
        this.type = columnType;
    }

    /**
     * Returns a Name of the column.
     * 
     * @return Column Name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a Size of the column.
     * 
     * @return Column Size.
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns if current column supports (has) size definition.
     * 
     * @return True if current column has predefined size or False otherwise.
     */
    public boolean isHasSize() {
        return hasSize;
    }

    /**
     * Returns a String representation of the column's type.
     * 
     * @return Column Type.
     */
    public SqlTypes getType() {
        return type;
    }

    /**
     * Returns if current column supports (has) default value.
     * 
     * @return True if current column has predefined value or False otherwise.
     */
    public boolean isHasDefaultValue() {
        return hasDefaultValue;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 17;
        result = prime * result + (autoincrement ? 1231 : 1237);
        result = prime * result + (defaultValue == null ? 0 : defaultValue.hashCode());
        result = prime * result + (hasDefaultValue ? 1231 : 1237);
        result = prime * result + (hasSize ? 1231 : 1237);
        result = prime * result + (name == null ? 0 : name.hashCode());
        result = prime * result + (nullable ? 1231 : 1237);
        result = prime * result + size;
        result = prime * result + (type == null ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        return this == obj || obj instanceof ColumnMetaData && areNameAndTypeDefined()
                && areColumnParametersEqual((ColumnMetaData) obj);
    }

    /**
     * Checks if name and type for the instance are defined.
     * 
     * @return true if name and type are not null or false otherwise.
     */
    private boolean areNameAndTypeDefined() {
        return name != null && type != null;
    }

    /**
     * Checks if column parameters equal for this and given obj.
     * 
     * @param obj
     *            an instance of ColumnMetaData which parameters will be compared to parameters of this
     * @return true if parameters equal or false otherwise.
     */
    private boolean areColumnParametersEqual(final ColumnMetaData obj) {
        return autoincrement == obj.autoincrement && hasSize == obj.hasSize && nullable == obj.nullable
                && hasDefaultValue == obj.hasDefaultValue && size == obj.size && type == obj.type
                && name.equals(obj.name) && (defaultValue == null || defaultValue.equals(obj.defaultValue));
    }

    @Override
    public String toString() {
        return "ColumnMetaData [nullable=" + nullable + ", autoincrement=" + autoincrement + ", hasDefaultValue="
                + hasDefaultValue + ", defaultValue=" + defaultValue + ", name=" + name + ", size=" + size
                + ", hasSize=" + hasSize + ", type=" + type + "]";
    }

    /**
     * Returns a String representation of the column's default value.
     * 
     * @return Column Default Value.
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * Returns if current column can keep null values.
     * 
     * @return True if current column can contain null values or False otherwise.
     */
    public boolean isNullable() {
        return nullable;
    }

    /**
     * Returns if current column is auto incremental.
     * 
     * @return True if current column is auto incremental or False otherwise.
     */
    public boolean isAutoincrement() {
        return autoincrement;
    }

    /**
     * Sets that the column can keep null values.
     * 
     * @param nullable
     *            Defines if column can keep null values.
     * @return The object itself.
     */
    public ColumnMetaData setNullable(final boolean nullable) {
        this.nullable = nullable;
        return this;
    }

    /**
     * Defines the size of the column.
     * 
     * @param size
     *            Column's size.
     * @return The object itself.
     */
    public ColumnMetaData setSize(final int size) {
        this.size = size;
        this.hasSize = size > 0;
        return this;
    }

    /**
     * Sets default value parameter for the column.
     * 
     * @param defaultValue
     *            Default's value for the column. Null means the column has no default value.
     * @return The object itself.
     */
    public ColumnMetaData setDefaultValue(final String defaultValue) {
        this.defaultValue = defaultValue;
        this.hasDefaultValue = defaultValue != null;
        return this;
    }

    /**
     * Sets that the column under the building should be auto incremental.
     * 
     * @param autoincrement
     *            Defines if column is auto incremental.
     * @return The object itself.
     */
    public ColumnMetaData setAutoincrement(final boolean autoincrement) {
        this.autoincrement = autoincrement;
        return this;
    }

    private boolean nullable;
    private boolean autoincrement;
    private boolean hasDefaultValue;
    private String defaultValue;
    private final String name;
    private int size;
    private boolean hasSize;
    private final SqlTypes type;

}
