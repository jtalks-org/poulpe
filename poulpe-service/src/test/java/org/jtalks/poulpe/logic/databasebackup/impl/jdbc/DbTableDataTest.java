package org.jtalks.poulpe.logic.databasebackup.impl.jdbc;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.jtalks.poulpe.logic.databasebackup.impl.dto.TableRow;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;

/**
 * Tests getting database data functionality for the DbTable class.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class DbTableDataTest {
    private final DataSource dataSource = mock(DataSource.class);
    private final Connection connection = mock(Connection.class);
    private final PreparedStatement statement = mock(PreparedStatement.class);
    private final ResultSet resultSet = mock(ResultSet.class);
    private final ResultSetMetaData resultSetMetaData = mock(ResultSetMetaData.class);

    /**
     * Current position in ResultSet is stored in resultSetPosition (with initial position before start of the records
     * so first ResultSet.next() moves position pointer to the first (0) position).
     */
    private int resultSetPosition = -1;
    private static final List<MetaDataItem> META_DATA = Lists.newArrayList();
    private static final List<RowDataItem> ROWS_DATA = Lists.newArrayList();
    private final List<TableRow> expectedData = Lists.newArrayList();

    /**
     * Sets up the stub data for (1) primary and foreign keys into the TABLE_KEYS_DATA and (2) available common
     * parameters into the COMMON_PARAMETERS_DATA.
     * 
     * @throws SQLException
     *             Usually is thrown if there is an error during collaborating with the database. For the test should
     *             never happen.
     */
    @BeforeClass
    @SuppressWarnings("rawtypes")
    private void setupStubData() throws SQLException {
        META_DATA.add(new MetaDataItem("id", java.sql.Types.INTEGER));
        META_DATA.add(new MetaDataItem("name", java.sql.Types.VARCHAR));

        ROWS_DATA.add(new RowDataItem(Lists.newArrayList("1", "name1")));
        ROWS_DATA.add(new RowDataItem(Lists.newArrayList("2", "name2")));
        ROWS_DATA.add(new RowDataItem(Lists.newArrayList("3", "name3")));

        /*
         * Define mock objects
         */
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.getMetaData()).thenReturn(resultSetMetaData);

        when(resultSetMetaData.getColumnCount()).thenReturn(META_DATA.size());
        when(resultSetMetaData.getColumnType(anyInt())).thenAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return META_DATA.get((Integer) args[0] - 1).columnType;
            }
        });
        when(resultSetMetaData.getColumnName(anyInt())).thenAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return META_DATA.get((Integer) args[0] - 1).columnName;
            }
        });

        when(resultSet.next()).thenAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) {
                return ++resultSetPosition < ROWS_DATA.size();
            }
        });
        when(resultSet.getObject(anyInt())).thenAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                List<String> row = ROWS_DATA.get(resultSetPosition).row;
                return row.get((Integer) args[0] - 1);
            }
        });

        /*
         * Prepare expected result.
         */
        for (MetaDataItem metaDataItem : META_DATA) {
        }
        for (RowDataItem item : ROWS_DATA) {
        }
        expectedData.add(new TableRow().addColumn("id", "1").addColumn("name", "'name1'"));
        expectedData.add(new TableRow().addColumn("id", "2").addColumn("name", "'name2'"));
        expectedData.add(new TableRow().addColumn("id", "3").addColumn("name", "'name3'"));
    }

    /**
     * The method tests that a DatabaseTableInfo object returns correct data for the table.
     * 
     * @throws SQLException
     *             Usually is thrown if there is an error during collaborating with the database. For the test should
     *             never happen.
     */
    @Test
    public void getDataTest() throws SQLException {
        DbTable testObject = new DbTable(dataSource, "tableName");
        assertEquals(expectedData, testObject.getData());
    }

    /**
     * Helper-class to store and provide stub information about meta data information.
     * 
     * @author Evgeny Surovtsev
     */
    class MetaDataItem {
        /**
         * Construct new instance with provided colimn's name and type.
         * 
         * @param columnName
         * @param columnType
         */
        public MetaDataItem(final String columnName, final int columnType) {
            super();
            this.columnName = columnName;
            this.columnType = columnType;
        }

        String columnName;
        Integer columnType;
    }

    /**
     * Helper-class to store and provide stub information about row data information.
     * 
     * @author Evgeny Surovtsev
     */
    class RowDataItem {
        /**
         * Construct new instance with provided list of strings which represent one row.
         * 
         * @param row
         *            Each element in the list represents a column's value for a given row.
         */
        RowDataItem(final List<String> row) {
            this.row = row;
        }

        List<String> row;
    }
}
