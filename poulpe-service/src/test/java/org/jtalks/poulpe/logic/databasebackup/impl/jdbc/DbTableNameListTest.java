package org.jtalks.poulpe.logic.databasebackup.impl.jdbc;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;
import javax.sql.DataSource;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * The class tests {@link DbTableNameList} by performing tests: (1) if a list returned from
 * DatabaseTableList.getPlainList contains all entries; (2) if method DatabaseTableList.getIndependentList resolves
 * table dependencies.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class DbTableNameListTest {

    /**
     * The method tests if {@link DatabaseTableList.getList()} enumerates returns all tables.
     * 
     * @throws SQLException
     *             Usually is thrown if there is an error during collaborating with the database. For the test should
     *             never happen.
     */
    @Test
    public void tableListReturnsAllTableNames() throws SQLException {
        List<String> source = Lists.newArrayList(getTableNameList(TABLE_INFO_LIST));
        Collections.sort(source);

        List<String> result = DbTableNameList.getPlainList(dataSource);
        Collections.sort(result);

        assertEquals(source, result);
        verify(tablesResultSet, atLeastOnce()).close();
        verify(connection, atLeastOnce()).close();

    }

    /**
     * The method tests if {@link DatabaseTableList.getList()} enumerates and returns tables in the order so tables on
     * the top of the list are not dependent on the tables from the bottom.
     * 
     * @throws SQLException
     *             Usually is thrown if there is an error during collaborating with the database. For the test should
     *             never happen.
     */
    @Test
    public void tableListResolvesDependencies() throws SQLException {
        List<String> previousTables = Lists.newArrayList();
        for (String tableName : DbTableNameList.getIndependentList(dataSource)) {
            TableInfoData tableInfoData = getTableInfoDataFromList(TABLE_INFO_LIST, tableName);
            // check if the tables which need to create tableName were met already (i.e. previousTables contains all
            // dependent tables).
            for (String dependentTableName : tableInfoData.tableDependencies) {
                // if a table has dependent table then dependent table should be upper (it should be in previousTables)
                assertTrue(previousTables.contains(dependentTableName));
            }
            previousTables.add(tableName);
        }
        verify(tablesResultSet, atLeastOnce()).close();
        verify(foreignKeys, atLeastOnce()).close();
        verify(connection, atLeastOnce()).close();
    }

    /**
     * Method defines stub data for the tests: (1) What a JDBC's {@link java.sql.DatabaseMetaData} should return for the
     * getTables() method. (2) Which result should be got from the class under testing.
     */
    @BeforeClass
    private void setupStubData() {
        TABLE_INFO_LIST.add(new TableInfoData("table1", null));
        TABLE_INFO_LIST.add(new TableInfoData("table2", Arrays.asList(new String[] { "table1" })));
        TABLE_INFO_LIST.add(new TableInfoData("table3", Arrays.asList(new String[] { "table1" })));
        TABLE_INFO_LIST.add(new TableInfoData("table4", Arrays.asList(new String[] { "table1" })));
        TABLE_INFO_LIST.add(new TableInfoData("table5", Arrays.asList(new String[] { "table1" })));
    }

    /**
     * Method reset the iterator before a new test. Iterator is used to support ResultSet.next() stubs.
     */
    @BeforeMethod
    private void setUpIterators() {
        resultSetPosition = -1;
    }

    /**
     * The method sets up Mock object which are used in the tests.
     * 
     * @throws SQLException
     *             Usually is thrown if there is an error during collaborating with the database. For the test should
     *             never happen.
     */
    @BeforeClass
    @SuppressWarnings("rawtypes")
    private void setupMockObjects() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.getMetaData()).thenReturn(dbMetaData);
        when(dbMetaData.getTables(anyString(), anyString(), anyString(), eq(new String[] { "TABLE" }))).thenReturn(
                tablesResultSet);

        // Get list of tables stubs
        when(tablesResultSet.next()).thenAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) {
                return ++resultSetPosition < TABLE_INFO_LIST.size();
            }
        });
        when(tablesResultSet.getString("TABLE_NAME")).thenAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) {
                return TABLE_INFO_LIST.get(resultSetPosition).tableName;
            }
        });

        // get foreign keys stubs
        when(dbMetaData.getImportedKeys(anyString(), anyString(), anyString())).thenAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                String foreignKeysTable = (String) args[2];
                if (!foreignKeysTable.equals(foreignKeysSelectedTable)) {
                    foreignKeysSelectedTable = (String) args[2];
                    resultSetPosition = -1;
                }
                return foreignKeys;
            }
        });
        when(foreignKeys.next()).thenAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) {
                TableInfoData tableInfoData = getTableInfoDataFromList(TABLE_INFO_LIST, foreignKeysSelectedTable);
                return ++resultSetPosition < tableInfoData.tableDependencies.size();
            }
        });
        when(foreignKeys.getString("FK_NAME")).thenAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) {
                TableInfoData tableInfoData = getTableInfoDataFromList(TABLE_INFO_LIST, foreignKeysSelectedTable);
                return tableInfoData.tableDependencies.get(resultSetPosition);
            }
        });
        when(foreignKeys.getString("FKCOLUMN_NAME")).thenReturn("FKCOLUMN_NAME");
        when(foreignKeys.getString("PKTABLE_NAME")).thenAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) {
                return foreignKeysSelectedTable;
            }
        });
        when(foreignKeys.getString("PKCOLUMN_NAME")).thenReturn("PKCOLUMN_NAME");

    }

    /**
     * Converts a given list of TableInfoData into list of Strings where each String represents a table name.
     * 
     * @param source
     *            A source list of TableInfoData which will be converted.
     * @return A list of Table names.
     */
    private List<String> getTableNameList(final List<TableInfoData> source) {
        List<String> result = Lists.transform(source, new Function<TableInfoData, String>() {
            @Override
            public String apply(@Nullable final TableInfoData arg) {
                return arg.tableName;
            }
        });
        return result;
    }

    /**
     * Looks for and returns a TableInfoData object with certain table name from the given list of TableInfoData.
     * 
     * @param source
     *            A list of TableInfoData to look for a certain table name.
     * @param tableName
     *            A table name for which a TableInfoData should be returned.
     * @return A TableInfoData instance from the source list which (TableInfoData) has defined table name.
     */
    private TableInfoData getTableInfoDataFromList(final List<TableInfoData> source, final String tableName) {
        for (TableInfoData tableInfoData : source) {
            if (tableName.equals(tableInfoData.tableName)) {
                return tableInfoData;
            }
        }
        throw new IllegalArgumentException("Given tableName=" + tableName + " is incorrect.");
    }

    private final ResultSet foreignKeys = mock(ResultSet.class);
    private final ResultSet tablesResultSet = mock(ResultSet.class);
    private final DatabaseMetaData dbMetaData = mock(DatabaseMetaData.class);
    private final Connection connection = mock(Connection.class);
    private final DataSource dataSource = mock(DataSource.class);

    /**
     * what a dbMetaData.getTables() should return.
     */
    private static final List<TableInfoData> TABLE_INFO_LIST = Lists.newArrayList();

    /**
     * current position of TablesResultSet within DBMetaData. each calling to tablesResultSet.next() increases the
     * position by one.
     */
    private int resultSetPosition;

    /**
     * The table name for which a dbMetaData.getImportedKeys was called at last.
     */
    private String foreignKeysSelectedTable;

    /**
     * Helper-class to store and provide stub information about database tables. In production the information provided
     * by TableInfoData comes from JDBC's DatabaseMetaData.getTables() and DatabaseMetaData.getImportedKeys() methods.
     * 
     * @author Evgeny Surovtsev
     * 
     */
    class TableInfoData {
        /**
         * Construct entity of TableInfoData with provided Table Name and Table's Dependencies.
         * 
         * @param tableName
         *            The name of the table.
         * @param tableDependencies
         *            The list contains dependencies for the given table.
         */
        TableInfoData(final String tableName, final List<String> tableDependencies) {
            this.tableName = tableName;
            if (tableDependencies == null) {
                this.tableDependencies = Lists.newArrayList();
            } else {
                this.tableDependencies = Lists.newArrayList(tableDependencies);
            }
        }

        private final String tableName;
        private List<String> tableDependencies;

    }
}
