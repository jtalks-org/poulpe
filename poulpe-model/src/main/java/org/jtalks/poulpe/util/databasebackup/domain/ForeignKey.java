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

/**
 * The class represent a Foreign key description data object. The class is immutable.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public final class ForeignKey implements TableKey {

    /**
     * Initiate an instance of the class with a given Foreign Key information.
     * 
     * @param fkTableName
     *            A Table which contain Foreign key.
     * @param fkColumnName
     *            A Foreign Key value.
     * @param pkTableName
     *            A Table which contain Primary key.
     * @param pkColumnName
     *            A Primary Key value.
     * @throws NullPointerException
     *             If any of fkTableName, fkColumnName, pkTableName, pkColumnName is null.
     */
    public ForeignKey(final String fkTableName, final String fkColumnName, final String pkTableName,
            final String pkColumnName) {
        Validate.notNull(fkTableName, "fkTableName must not be null");
        Validate.notNull(fkColumnName, "fkColumnName must not be null");
        Validate.notNull(pkTableName, "pkTableName must not be null");
        Validate.notNull(pkColumnName, "pkColumnName must not be null");
        this.fkTableName = fkTableName;
        this.fkColumnName = fkColumnName;
        this.pkTableName = pkTableName;
        this.pkColumnName = pkColumnName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(31, 17)
                .append(fkColumnName)
                .append(fkTableName)
                .append(pkColumnName)
                .append(pkTableName)
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
        ForeignKey other = (ForeignKey) obj;
        return new EqualsBuilder()
                .append(this.fkColumnName, other.fkColumnName)
                .append(this.fkTableName, other.fkTableName)
                .append(this.pkColumnName, other.pkColumnName)
                .append(this.pkTableName, other.pkTableName)
                .build();
    }

    /**
     * Returns a foreign key table name.
     * 
     * @return Foreign table name.
     */
    public String getFkTableName() {
        return fkTableName;
    }

    /**
     * Returns a foreign key value.
     * 
     * @return Foreign key.
     */
    public String getFkColumnName() {
        return fkColumnName;
    }

    /**
     * Returns a primary key table name.
     * 
     * @return Primary table name.
     */
    public String getPkTableName() {
        return pkTableName;
    }

    /**
     * Returns a primary key value.
     * 
     * @return Primary key.
     */
    public String getPkColumnName() {
        return pkColumnName;
    }

    private final String fkTableName;
    private final String fkColumnName;
    private final String pkTableName;
    private final String pkColumnName;
}
