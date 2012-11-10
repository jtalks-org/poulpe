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
        if (fkTableName == null || fkColumnName == null || pkTableName == null || pkColumnName == null) {
            throw new NullPointerException("Fields should be initialized: fkTableName=" + fkTableName
                    + " fkColumnName=" + fkColumnName + "pkTableName=" + pkTableName + " pkColumnName=" + pkColumnName);
        }
        this.fkTableName = fkTableName;
        this.fkColumnName = fkColumnName;
        this.pkTableName = pkTableName;
        this.pkColumnName = pkColumnName;
    }

    @Override
    public String toString() {
        return "[fkTableName=" + fkTableName + ", fkColumnName=" + fkColumnName + ", pkTableName="
                + pkTableName + ", pkColumnName=" + pkColumnName + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 17;
        result = prime * result + ((fkColumnName == null) ? 0 : fkColumnName.hashCode());
        result = prime * result + ((fkTableName == null) ? 0 : fkTableName.hashCode());
        result = prime * result + ((pkColumnName == null) ? 0 : pkColumnName.hashCode());
        result = prime * result + ((pkTableName == null) ? 0 : pkTableName.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        return (this == obj)
                || (obj instanceof ForeignKey
                        && fkColumnName != null
                        && fkTableName != null
                        && pkColumnName != null
                        && pkTableName != null

                        && fkColumnName.equals(((ForeignKey) obj).fkColumnName)
                        && fkTableName.equals(((ForeignKey) obj).fkTableName)
                        && pkColumnName.equals(((ForeignKey) obj).pkColumnName)
                        && pkTableName.equals(((ForeignKey) obj).pkTableName));
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
