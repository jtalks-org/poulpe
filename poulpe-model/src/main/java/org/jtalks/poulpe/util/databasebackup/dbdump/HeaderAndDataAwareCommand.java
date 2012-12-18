package org.jtalks.poulpe.util.databasebackup.dbdump;

import java.sql.SQLException;

public abstract class HeaderAndDataAwareCommand implements DbDumpCommand {

    @Override
    public final StringBuilder execute() throws SQLException {
        return new StringBuilder()
                .append(getHeader())
                .append(DbDumpUtil.LINEFEED)
                .append(getData())
                .append(DbDumpUtil.LINEFEED)
                .append(DbDumpUtil.LINEFEED);
    }

    /**
     * Returns a description for the data which is provided by the command. The description will be used for formating
     * the whole result from the command executing.
     * 
     * @return text header for the provided data.
     */
    protected abstract StringBuilder getHeader();

    /**
     * Returns data, provided by the command. The date should dump the peace of information about table which is
     * provided (the peace of information) by the command.
     * 
     * @return provided data in the text shape.
     */
    protected abstract StringBuilder getData() throws SQLException;

}
