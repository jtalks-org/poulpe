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
 * The class is immutable.
 * 
 * @author surev01
 * 
 */
final class TableDataColumn {

    public static class Builder {

        public Builder(final String name, final String type) {
            super();
            this.name = name;
            this.type = type;
        }

        public Builder setNullable(final boolean nullable) {
            this.nullable = nullable;
            return this;
        }

        public Builder setSize(final int size) {
            this.size = size;
            this.hasSize = true;
            return this;
        }

        public Builder setDefaultValue(final String defaultValue) {
            this.defaultValue = defaultValue;
            this.hasDefaultValue = true;
            return this;
        }

        public Builder setAutoincrement(final boolean autoincrement) {
            this.autoincrement = autoincrement;
            return this;
        }

        public TableDataColumn build() {
            return new TableDataColumn(this);
        }

        private boolean nullable;
        private boolean autoincrement;
        private boolean hasDefaultValue;
        private String defaultValue;
        private final String name;
        private int size;
        private boolean hasSize;
        private final String type;
    }

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

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }

    public boolean isHasSize() {
        return hasSize;
    }

    public String getType() {
        return type;
    }

    public boolean isHasDefaultValue() {
        return hasDefaultValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public boolean isNullable() {
        return nullable;
    }

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

}