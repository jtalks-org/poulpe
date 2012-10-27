package org.jtalks.poulpe.logic.databasebackup.impl.jdbc;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Nullable;
import javax.sql.DataSource;

import org.jtalks.poulpe.logic.databasebackup.impl.dto.TableForeignKey;
import org.jtalks.poulpe.logic.databasebackup.impl.dto.TablePrimaryKey;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * Tests getting database keys (primary, foreign) functionality for the DbTable class.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class DbTableKeysTest {

    private final DataSource dataSource = mock(DataSource.class);
    /*
     * Current position in ResultSet is stored in resultSetPosition (with initial position before start of the records
     * so first ResultSet.next() moves position pointer to the first (0) position).
     */
    private int resultSetPosition = -1;
    private static final List<TableKeysData> TABLE_KEYS_DATA = Lists.newArrayList();

    /**
     * Sets up the stub data for (1) primary and foreign keys into the TABLE_KEYS_DATA and (2) available common
     * parameters into the COMMON_PARAMETERS_DATA.
     */
    @BeforeClass
    private void setupStubData() {
        TABLE_KEYS_DATA.clear();
        TABLE_KEYS_DATA.add(new TableKeysData("fkTable", "fkColumn1", "prTable", "prColumn1"));
        TABLE_KEYS_DATA.add(new TableKeysData("fkTable", "fkColumn2", "prTable", "prColumn2"));
        TABLE_KEYS_DATA.add(new TableKeysData("fkTable", "fkColumn3", "prTable", "prColumn3"));
    }

    /**
     * The method tests that a DatabaseTableInfo object returns correct Primary Keys List.
     * 
     * @throws SQLException
     *             Usually is thrown if there is an error during collaborating with the database. For the test should
     *             never happen.
     */
    @Test
    @SuppressWarnings("rawtypes")
    public void getPrimaryKeyListTest() throws SQLException {
        resultSetPosition = -1;
        /*
         * Define mock objects
         */
        Connection connection = mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);

        DatabaseMetaData dbMetaData = mock(DatabaseMetaData.class);
        when(connection.getMetaData()).thenReturn(dbMetaData);

        ResultSet primaryKeysResultSet = mock(ResultSet.class);
        when(dbMetaData.getPrimaryKeys(anyString(), anyString(), eq("tableName"))).thenReturn(primaryKeysResultSet);

        when(primaryKeysResultSet.next()).thenAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) {
                return ++resultSetPosition < TABLE_KEYS_DATA.size();
            }
        });
        when(primaryKeysResultSet.getString("PK_NAME")).thenAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) {
                return TABLE_KEYS_DATA.get(resultSetPosition).pkTableName;
            }
        });
        when(primaryKeysResultSet.getString("COLUMN_NAME")).thenAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) {
                return TABLE_KEYS_DATA.get(resultSetPosition).pkColumnName;
            }
        });

        /*
         * Preparing expected result (based on the info defined in TABLE_KEYS_DATA).
         */
        List<TablePrimaryKey> expectedPrimaryKeyList = Lists.transform(TABLE_KEYS_DATA,
                new Function<TableKeysData, TablePrimaryKey>() {
                    @Override
                    public TablePrimaryKey apply(@Nullable final TableKeysData arg) {
                        return new TablePrimaryKey(arg.pkColumnName);
                    }
                });

        /*
         * Performing the test itself.
         */
        DbTable testObject = new DbTable(dataSource, "tableName");
        assertEquals(expectedPrimaryKeyList, testObject.getPrimaryKeyList());
        verify(primaryKeysResultSet).close();
        verify(connection).close();
    }

    /**
     * The method tests that a DatabaseTableInfo object returns correct Primary Keys List.
     * 
     * @throws SQLException
     *             Usually is thrown if there is an error during collaborating with the database. For the test should
     *             never happen.
     */
    @Test
    @SuppressWarnings("rawtypes")
    public void getForeignKeyListTest() throws SQLException {
        resultSetPosition = -1;
        /*
         * Define mock objects
         */
        Connection connection = mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connection);

        DatabaseMetaData dbMetaData = mock(DatabaseMetaData.class);
        when(connection.getMetaData()).thenReturn(dbMetaData);

        ResultSet foreignKeysResultSet = mock(ResultSet.class);
        when(dbMetaData.getImportedKeys(anyString(), anyString(), eq("tableName"))).thenReturn(foreignKeysResultSet);

        when(foreignKeysResultSet.next()).thenAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) {
                return ++resultSetPosition < TABLE_KEYS_DATA.size();
            }
        });
        when(foreignKeysResultSet.getString("FK_NAME")).thenAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) {
                return TABLE_KEYS_DATA.get(resultSetPosition).fkTableName;
            }
        });
        when(foreignKeysResultSet.getString("FKCOLUMN_NAME")).thenAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) {
                return TABLE_KEYS_DATA.get(resultSetPosition).fkColumnName;
            }
        });
        when(foreignKeysResultSet.getString("PKTABLE_NAME")).thenAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) {
                return TABLE_KEYS_DATA.get(resultSetPosition).pkTableName;
            }
        });
        when(foreignKeysResultSet.getString("PKCOLUMN_NAME")).thenAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) {
                return TABLE_KEYS_DATA.get(resultSetPosition).pkColumnName;
            }
        });

        /*
         * Preparing expected result (based on the info defined in TABLE_KEYS_DATA).
         */
        List<TableForeignKey> expectedForeignKeyList = Lists.transform(TABLE_KEYS_DATA,
                new Function<TableKeysData, TableForeignKey>() {
                    @Override
                    public TableForeignKey apply(@Nullable final TableKeysData arg) {
                        return new TableForeignKey(arg.fkTableName, arg.fkColumnName,
                                arg.pkTableName, arg.pkColumnName);
                    }
                });

        /*
         * Performing the test itself.
         */
        DbTable testObject = new DbTable(dataSource, "tableName");
        assertEquals(expectedForeignKeyList, testObject.getForeignKeyList());
        verify(foreignKeysResultSet).close();
        verify(connection).close();
    }

    /**
     * Helper-class to store and provide stub information about table's keys.
     * 
     * @author Evgeny Surovtsev
     */
    class TableKeysData {
        /**
         * Construct and initialize a new instance of TableKeysData with given key's information.
         * 
         * @param fkTableName
         *            Foreign key's database table name.
         * @param fkColumnName
         *            Foreign key table's column name.
         * @param pkTableName
         *            Primary key's database table name.
         * @param pkColumnName
         *            Primary key table's column name.
         */
        public TableKeysData(final String fkTableName, final String fkColumnName, final String pkTableName,
                final String pkColumnName) {
            this.fkTableName = fkTableName;
            this.fkColumnName = fkColumnName;
            this.pkTableName = pkTableName;
            this.pkColumnName = pkColumnName;
        }

        final String fkTableName;
        final String fkColumnName;
        final String pkTableName;
        final String pkColumnName;
    }
}
