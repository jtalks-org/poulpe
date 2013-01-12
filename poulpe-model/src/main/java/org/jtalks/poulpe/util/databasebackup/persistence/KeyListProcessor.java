package org.jtalks.poulpe.util.databasebackup.persistence;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import org.jtalks.poulpe.util.databasebackup.domain.TableKey;
import org.springframework.jdbc.support.DatabaseMetaDataCallback;
import org.springframework.jdbc.support.MetaDataAccessException;

import com.google.common.collect.Sets;

/**
 * Class reads information about table keys for given table Set and creates a list of TableKeys. Class is used to get
 * rid of duplication while obtaining different types of keys (generally Primary, Foreign and Unique).
 * 
 * @author Evgeny Surovtsev
 * 
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
    public KeyListProcessor(final String tableName, final TableKeyPerformer tableKeyPerformer) {
        super();
        assert tableKeyPerformer != null;
        assert tableName != null;
        this.tableKeyPerformer = tableKeyPerformer;
        this.tableName = tableName;
    }

    @Override
    public Object processMetaData(final DatabaseMetaData dmd) throws SQLException, MetaDataAccessException {
        final Set<TableKey> tableKeySet = Sets.newHashSet();
        final ResultSet rs = tableKeyPerformer.getResultSet(dmd, tableName);
        while (rs.next()) {
            tableKeyPerformer.addKeyToSet(rs, tableKeySet);
        }
        return tableKeySet;
    }

    private final TableKeyPerformer tableKeyPerformer;
    private final String tableName;
}
