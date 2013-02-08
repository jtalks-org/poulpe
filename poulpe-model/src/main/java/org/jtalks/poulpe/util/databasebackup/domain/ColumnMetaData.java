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

import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jtalks.poulpe.util.databasebackup.persistence.SqlTypes;

import com.google.common.collect.Maps;

/**
 * The class describes structure of one table's Column. So every table has a number of columns and each column is
 * described by a particular ColumnMetaData object.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public final class ColumnMetaData {
    /**
     * Returns an instance of ColumnMetaData for given columnName and columnType. This method controls of count of
     * ColumnMetaData objects created.
     * 
     * @param columnName
     *            The name of the table column.
     * @param columnType
     *            The type of the table column.
     * @return an instance of ColumnMetaData
     */
    public static ColumnMetaData getInstance(String columnName, SqlTypes columnType) {
        Validate.notBlank(columnName, "columnName must not be null");
        Validate.notNull(columnType, "columnType must not be null");

        String key = columnName + ":" + columnType.toString();
        if (columnMetaDataMap.containsKey(key)) {
            return columnMetaDataMap.get(key);
        }

        ColumnMetaData columnMetaData = new ColumnMetaData(columnName, columnType);
        columnMetaDataMap.put(key, columnMetaData);
        return columnMetaData;
    }

    /**
     * Constructs a new Column meta data object with two given obligatory parameters: table column's name and type.
     * Other parameters should be set via setters.
     * 
     * @param columnName
     *            The name of the table column.
     * @param columnType
     *            The type of the table column.
     */
    private ColumnMetaData(final String columnName, final SqlTypes columnType) {
        assert columnName != null : "columnName must not be null";
        assert columnName.length() > 0 : "columnName must not be empty";
        assert columnType != null : "columnType must not be null";

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
        return new HashCodeBuilder(31, 17)
                .append(autoincrement)
                .append(defaultValue)
                .append(hasDefaultValue)
                .append(hasSize)
                .append(name)
                .append(nullable)
                .append(size)
                .append(type)
                .append(comment)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        ColumnMetaData other = (ColumnMetaData) obj;
        return new EqualsBuilder()
                .append(this.autoincrement, other.autoincrement)
                .append(this.defaultValue, other.defaultValue)
                .append(this.hasDefaultValue, other.hasDefaultValue)
                .append(this.hasSize, other.hasSize)
                .append(this.name, other.name)
                .append(this.nullable, other.nullable)
                .append(this.size, other.size)
                .append(this.type, other.type)
                .append(this.comment, other.comment)
                .isEquals();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
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

    /**
     * Sets the comment for the column.
     * 
     * @param comment
     *            a comment for the column.
     * @return this.
     */
    public ColumnMetaData setComment(final String comment) {
        this.comment = comment;
        return this;
    }

    /**
     * Returns the comment for the column.
     * 
     * @return a comment for the column.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Checks if the column has a comment.
     * 
     * @return true if the column has a comment of false otherwise.
     */
    public boolean hasComment() {
        return comment != null && comment.length() > 0;
    }

    private boolean nullable;
    private boolean autoincrement;
    private boolean hasDefaultValue;
    private String defaultValue;
    private final String name;
    private int size;
    private boolean hasSize;
    private final SqlTypes type;
    private String comment;

    private static Map<String, ColumnMetaData> columnMetaDataMap = Maps.newHashMap();
}
