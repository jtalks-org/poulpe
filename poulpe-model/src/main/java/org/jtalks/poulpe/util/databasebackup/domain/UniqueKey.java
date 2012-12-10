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
import java.util.Set;

import org.apache.commons.lang3.Validate;

import com.google.common.collect.Sets;

/**
 * The class represent a Primary or Unique key description data object. The class is immutable.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public final class UniqueKey implements TableKey {
    /**
     * Initiate an instance of the class with a given Primary Key value.
     * 
     * @param columnName
     *            A String that represents Primary Key value.
     * @throws NullPointerException
     *             If pkColumnName is null.
     */
    public UniqueKey(final String indexName, final String columnName) {
        Validate.notNull(indexName, "indexName must not be null");
        Validate.notNull(columnName, "columnName must not be null");
        this.indexName = indexName;
        this.columnNameSet = Sets.newHashSet(columnName);
    }

    /**
     * Initiate an instance of the class with a given Primary Key value.
     * 
     * @param columnName
     *            A String that represents Primary Key value.
     * @throws NullPointerException
     *             If pkColumnName is null.
     */
    public UniqueKey(final String indexName, final Set<String> columnNameSet) {
        Validate.notNull(indexName, "indexName must not be null");
        Validate.notNull(columnNameSet, "columnNameSet must not be null");
        Validate.notEmpty(columnNameSet, "columnNameSet must have at least 1 element");

        this.indexName = indexName;
        this.columnNameSet = Sets.newHashSet(columnNameSet);
    }

    /**
     * @return column name.
     */
    public Set<String> getColumnNameSet() {
        return Collections.unmodifiableSet(columnNameSet);
    }

    /**
     * @return index name.
     */
    public String getIndexName() {
        return indexName;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (columnNameSet == null ? 0 : columnNameSet.hashCode());
        result = 31 * result + (indexName == null ? 0 : indexName.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "UniqueKey [indexName=" + indexName + ", columnNameSet=" + columnNameSet + "]";
    }

    @Override
    public boolean equals(final Object obj) {
        return this == obj || columnNameSet != null && indexName != null && obj instanceof UniqueKey
                && columnNameSet.equals(((UniqueKey) obj).columnNameSet)
                && indexName.equals(((UniqueKey) obj).indexName);
    }

    private final String indexName;
    private final Set<String> columnNameSet;
}
