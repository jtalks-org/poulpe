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
package org.jtalks.poulpe.util.databasebackup.dbdump.mysql;

import java.io.IOException;
import java.io.Writer;
import java.sql.SQLException;

import org.apache.commons.lang3.Validate;
import org.jtalks.poulpe.util.databasebackup.dbdump.HeaderAndDataAwareCommand;
import org.jtalks.poulpe.util.databasebackup.domain.ForeignKey;
import org.jtalks.poulpe.util.databasebackup.persistence.DbTable;
import org.jtalks.poulpe.util.databasebackup.persistence.TableDataUtil;

/**
 * Command analyzing table foreign keys and push this info into {@link java.io.OutputStream OutputStream}
 */
public class AddForeignKeysCommand extends HeaderAndDataAwareCommand {

    /**
     * Constructor for initialization variables
     * 
     * @param dbTable
     *            target table
     */
    public AddForeignKeysCommand(final DbTable dbTable) {
        Validate.notNull(dbTable, "dbTable must not be null");
        this.dbTable = dbTable;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void putHeader(Writer writer) throws IOException {
        assert writer != null : "writer must not be null";
        StringBuilder header = new StringBuilder();
        header.append("--").append(LINEFEED);
        header.append("-- Foreign keys definition for table ");
        header.append(TableDataUtil.getSqlColumnQuotedString(dbTable.getTableName())).append(LINEFEED);
        header.append("--").append(LINEFEED);

        writer.write(header.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void putData(Writer writer) throws SQLException, IOException {
        assert writer != null : "writer must not be null";
        writer.write(getKeys());
    }

    /**
     * Formats string representation of the table keys (Primary, foreign and unique) so it can be used in the SQL CREATE
     * TABLE statement.
     * 
     * @return a string representation of the table keys
     * @throws SQLException
     *             if any error with database occurs
     */
    public String getKeys() throws SQLException {
        StringBuilder result = new StringBuilder();

        if (dbTable.getForeignKeySet().size() > 0) {
            for (ForeignKey key : dbTable.getForeignKeySet()) {
                result.append(String.format(FOREIGN_KEY_TEMPLATE,
                        TableDataUtil.getSqlColumnQuotedString(dbTable.getTableName()),
                        TableDataUtil.getSqlColumnQuotedString(key.getFkColumnName()),
                        TableDataUtil.getSqlColumnQuotedString(key.getPkTableName()),
                        TableDataUtil.getSqlColumnQuotedString(key.getPkColumnName())));
                result.append(LINEFEED);
            }
        }

        return result.toString();
    }

    private final DbTable dbTable;

    /**
     * %1 - tableName %2 - getFkColumnName %3 - getPkTableName %4 - getPkColumnName.
     */
    private static final String FOREIGN_KEY_TEMPLATE = "ALTER TABLE %1$s ADD FOREIGN KEY (%2$s) REFERENCES %3$s(%4$s);";
}
