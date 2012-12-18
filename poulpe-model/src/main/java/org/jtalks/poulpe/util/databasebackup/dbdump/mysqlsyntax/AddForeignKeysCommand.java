package org.jtalks.poulpe.util.databasebackup.dbdump.mysqlsyntax;

import static org.jtalks.poulpe.util.databasebackup.dbdump.DbDumpUtil.LINEFEED;

import java.sql.SQLException;

import org.apache.commons.lang.Validate;
import org.jtalks.poulpe.util.databasebackup.dbdump.HeaderAndDataAwareCommand;
import org.jtalks.poulpe.util.databasebackup.domain.ForeignKey;
import org.jtalks.poulpe.util.databasebackup.persistence.DbTable;
import org.jtalks.poulpe.util.databasebackup.persistence.TableDataUtil;

public class AddForeignKeysCommand extends HeaderAndDataAwareCommand {

    public AddForeignKeysCommand(final DbTable dbTable) {
        Validate.notNull(dbTable, "dbTable must not be null");
        this.dbTable = dbTable;
    }

    @Override
    protected StringBuilder getHeader() {
        StringBuilder header = new StringBuilder();
        header.append("--").append(LINEFEED);
        header.append("-- Foreign keys definition for table ");
        header.append(TableDataUtil.getSqlColumnQuotedString(dbTable.getTableName())).append(LINEFEED);
        header.append("--").append(LINEFEED);
        return header;
    }

    @Override
    protected StringBuilder getData() throws SQLException {
        StringBuilder data = new StringBuilder();
        data.append(getKeys());
        return data;
    }

    /**
     * Formats string representation of the table keys (Primary, foreign and unique) so it can be used in the SQL CREATE
     * TABLE statement.
     * 
     * @return a string representation of the table keys
     * @throws SQLException
     *             if any error with database occurs
     */
    public StringBuilder getKeys() throws SQLException {
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

        return result;
    }

    private final DbTable dbTable;

    /**
     * %1 - tableName %2 - getFkColumnName %3 - getPkTableName %4 - getPkColumnName.
     */
    private static final String FOREIGN_KEY_TEMPLATE = "ALTER TABLE %1$s ADD FOREIGN KEY (%2$s) REFERENCES %3$s(%4$s);";
}
