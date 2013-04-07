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

package org.jtalks.poulpe.util.databasebackup.persistence;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.jtalks.poulpe.util.databasebackup.common.collection.Sets;
import org.jtalks.poulpe.util.databasebackup.domain.TableKey;
import org.springframework.jdbc.support.DatabaseMetaDataCallback;

/**
 * Class reads information about table keys for given table Set and creates a list of TableKeys. Class is used to get
 * rid of duplication while obtaining different types of keys (generally Primary, Foreign and Unique).
 * 
 * @author Evgeny Surovtsev
 */
final class KeyListProcessor implements DatabaseMetaDataCallback {

    /**
     * Initializes KeyListProcessor with given table name and TableKeyPerformer.
     * 
     * @param tableName
     *            The name of the table.
     * @param tableKeyPerformer
     *            An instance of specific Key Performer (Strategy).
     */
    public KeyListProcessor(String tableName, TableKeyPerformer tableKeyPerformer) {
        Validate.notNull(tableKeyPerformer, "tableKeyPerformer must not be null");
        Validate.notNull(tableName, "tableName must not be null");
        this.tableKeyPerformer = tableKeyPerformer;
        this.tableName = tableName;
    }

    @Override
    public Object processMetaData(DatabaseMetaData dmd) throws SQLException {
        Set<TableKey> tableKeySet = Sets.newHashSet();
        ResultSet rs = null;
        try {
            rs = tableKeyPerformer.getResultSet(dmd, tableName);
            while (rs.next()) {
                tableKeyPerformer.addKeyToSet(rs, tableKeySet);
            }
        } catch (SQLException e) {
            throw new SQLException(e);
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
        return tableKeySet;
    }

    private final TableKeyPerformer tableKeyPerformer;
    private final String tableName;
}
