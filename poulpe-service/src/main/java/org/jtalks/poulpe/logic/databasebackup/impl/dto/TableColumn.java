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

/**
 * The class represent a Table's Data Column description data object. The class is immutable.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public final class TableColumn {
    /**
     * Construct Table Data Column object based on given Builder.
     * 
     * @param name
     *            The name of the table column.
     * @param type
     *            The type of the table column.
     */
    public TableColumn(final String name, final SqlTypes type) {
        if (name == null || type == null) {
            throw new NullPointerException("Fields should be initialized: name=" + name + " type=" + type);
        }
        this.name = name;
        this.type = type;
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
        result = prime * result + ((defaultValue == null) ? 0 : defaultValue.hashCode());
        result = prime * result + (hasDefaultValue ? 1231 : 1237);
        result = prime * result + (hasSize ? 1231 : 1237);
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + (nullable ? 1231 : 1237);
        result = prime * result + size;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        return (this == obj)
                || (obj instanceof TableColumn
                        && name != null
                        && type != null

                        && autoincrement == ((TableColumn) obj).autoincrement
                        && hasSize == ((TableColumn) obj).hasSize
                        && nullable == ((TableColumn) obj).nullable
                        && hasDefaultValue == ((TableColumn) obj).hasDefaultValue
                        && size == ((TableColumn) obj).size
                        && type == ((TableColumn) obj).type
                        && name.equals(((TableColumn) obj).name)
                        && ((defaultValue != null) ? defaultValue.equals(((TableColumn) obj).defaultValue) : true)
                );
    }

    @Override
    public String toString() {
        return "TableColumn [nullable=" + nullable + ", autoincrement=" + autoincrement + ", hasDefaultValue="
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
    public TableColumn setNullable(final boolean nullable) {
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
    public TableColumn setSize(final int size) {
        this.size = size;
        this.hasSize = size > 0;
        return this;
    }

    /**
     * Sets default value parameter for the column.
     * 
     * @param defaultValue
     *            Default's value for the column.
     * @return The object itself.
     */
    public TableColumn setDefaultValue(final String defaultValue) {
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
    public TableColumn setAutoincrement(final boolean autoincrement) {
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
