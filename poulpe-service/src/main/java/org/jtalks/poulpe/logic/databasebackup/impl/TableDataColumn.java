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

/**
 * The class represent a Table's Data Column description data object. The class is immutable.
 * 
 * @author Evgeny Surovtsev
 * 
 */
final class TableDataColumn {
    /**
     * Construct Table Data Column object based on given Builder.
     * 
     * @param builder
     *            An instance which will be used for constructing Table Data Column.
     */
    private TableDataColumn(final Builder builder) {
        nullable = builder.nullable;
        autoincrement = builder.autoincrement;
        hasDefaultValue = builder.hasDefaultValue;
        defaultValue = builder.defaultValue;
        name = builder.name;
        size = builder.size;
        hasSize = builder.hasSize;
        type = builder.type;
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
    public String getType() {
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

    private final boolean nullable;
    private final boolean autoincrement;
    private final boolean hasDefaultValue;
    private final String defaultValue;
    private final String name;
    private final int size;
    private final boolean hasSize;
    private final String type;

    /**
     * Using the Builder is the only way to to construct TableDataColumn object.
     * 
     * @author Evgeny Surovtsev
     * 
     */
    public static class Builder {

        /**
         * Prepare Builder object with obliged parameters.
         * 
         * @param name
         *            The name of the table column.
         * @param type
         *            The type of the table column.
         */
        public Builder(final String name, final String type) {
            super();
            this.name = name;
            this.type = type;
        }

        /**
         * Sets that the column under the building can keep nullable values.
         * 
         * @param nullable
         *            Defines if column can keep nullable values.
         * @return Instance of the same Builder.
         */
        public Builder setNullable(final boolean nullable) {
            this.nullable = nullable;
            return this;
        }

        /**
         * Defines the size of the column.
         * 
         * @param size
         *            Column's size.
         * @return Instance of the same Builder.
         */
        public Builder setSize(final int size) {
            this.size = size;
            this.hasSize = true;
            return this;
        }

        /**
         * Sets default value parameter for the column.
         * 
         * @param defaultValue
         *            Default's value for the column.
         * @return Instance of the same Builder.
         */
        public Builder setDefaultValue(final String defaultValue) {
            this.defaultValue = defaultValue;
            this.hasDefaultValue = true;
            return this;
        }

        /**
         * Sets that the column under the building should be auto incremental.
         * 
         * @param autoincrement
         *            Defines if column is auto incremental.
         * @return Instance of the same Builder.
         */
        public Builder setAutoincrement(final boolean autoincrement) {
            this.autoincrement = autoincrement;
            return this;
        }

        /**
         * Build the TableDataColumn object.
         * 
         * @return An instance of just built TableDataColumn.
         */
        public TableDataColumn build() {
            return new TableDataColumn(this);
        }

        private final String name;
        private final String type;

        private boolean nullable;
        private boolean autoincrement;
        private boolean hasDefaultValue;
        private String defaultValue;
        private int size;
        private boolean hasSize;
    }
}
