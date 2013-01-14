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

import org.jtalks.poulpe.util.databasebackup.domain.TableKey;

/**
 * Describes common interface for specific ways of table keys obtaining from ResultSets.
 * 
 * @author Evgeny Surovtsev
 * 
 */
interface TableKeyPerformer {
    /**
     * Returns result set which contains info about specific types of table keys. For example, ResultSet which points to
     * Foreign keys for the given table.
     * 
     * @param dmd
     *            An instance of DatabaseMetaData to obtain info from.
     * @param tableName
     *            The name of the table.
     * @return ResultSet which contains info about specific Table Keys.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    ResultSet getResultSet(DatabaseMetaData dmd, String tableName) throws SQLException;

    /**
     * Checks if current iterator of ResultSet contains info about specific table Key and add new instance of TableKey
     * to the given list, based on ResultSet under testing.
     * 
     * @param rs
     *            ResultSet which contains info about specific Table Keys.
     * @param keyList
     *            An existing List where an information about obtained Key will be put.
     * @throws SQLException
     *             Is thrown in case any errors during work with database occur.
     */
    void addKeyToSet(ResultSet rs, Set<TableKey> keyList) throws SQLException;
}
