package org.jtalks.poulpe.logic.databasebackup.impl.jdbc;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.annotation.Nullable;
import javax.sql.DataSource;

import org.jtalks.poulpe.logic.databasebackup.impl.dto.SqlTypes;
import org.jtalks.poulpe.logic.databasebackup.impl.dto.TableColumn;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * Tests getting database structure functionality for the DbTable class.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class DbTableStructureTest {

    private final DataSource dataSource = mock(DataSource.class);
    private final Connection connection = mock(Connection.class);
    private final DatabaseMetaData dbMetaData = mock(DatabaseMetaData.class);
    private final ResultSet tableMetaData = mock(ResultSet.class);
    private final Statement statement = mock(Statement.class);
    private final ResultSet resultSet = mock(ResultSet.class);
    private final ResultSetMetaData resultSetMetaData = mock(ResultSetMetaData.class);

    /**
     * Current position in ResultSet is stored in resultSetPosition (with initial position before start of the records
     * so first ResultSet.next() moves position pointer to the first (0) position).
     */
    private int resultSetPosition = -1;
    private static final List<ColumnStructure> STRUCTURE_DATA = Lists.newArrayList();
    private static List<TableColumn> expectedStructure;

    /**
     * Sets up the stub data for (1) primary and foreign keys into the TABLE_KEYS_DATA and (2) available common
     * parameters into the COMMON_PARAMETERS_DATA.
     * 
     * @throws SQLException
     *             Usually is thrown if there is an error during collaborating with the database. For the test should
     *             never happen.
     */
    @BeforeClass
    private void setupStubData() throws SQLException {
        /*
         * Define test dataSetting up mock objects.
         */
        STRUCTURE_DATA.add(new ColumnStructure("column1", java.sql.Types.VARCHAR, 32, "0", false,
                ResultSetMetaData.columnNoNulls));
        STRUCTURE_DATA.add(new ColumnStructure("column2", java.sql.Types.INTEGER, 8, "0", false,
                ResultSetMetaData.columnNoNulls));
        STRUCTURE_DATA.add(new ColumnStructure("column3", java.sql.Types.TIMESTAMP, 0, "", false,
                ResultSetMetaData.columnNoNulls));
        STRUCTURE_DATA.add(new ColumnStructure("column4", java.sql.Types.BLOB, 0, null, false,
                ResultSetMetaData.columnNullable));

        /*
         * Prepare expected result.
         */
        expectedStructure = Lists.transform(STRUCTURE_DATA, new Function<ColumnStructure, TableColumn>() {
            @Override
            public TableColumn apply(@Nullable final ColumnStructure arg) {
                TableColumn column = new TableColumn(arg.columnName,
                        SqlTypes.getSqlTypeByJdbcSqlType(arg.columnType));
                column.setAutoincrement(arg.isAutoIncrement);
                column.setDefaultValue((arg.columnDefaultValue != null
                        && arg.columnDefaultValue.length() > 0) ? arg.columnDefaultValue : null);
                column.setSize(arg.columnDisplaySize);
                column.setNullable(arg.isNullable == ResultSetMetaData.columnNullable);

                return column;
            }
        });

        /*
         * Define mock objects
         */
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.getMetaData()).thenReturn(dbMetaData);
        when(dbMetaData.getColumns(anyString(), anyString(), anyString(), anyString())).thenReturn(tableMetaData);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);
        when(resultSet.getMetaData()).thenReturn(resultSetMetaData);

        when(tableMetaData.next()).thenAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) {
                return ++resultSetPosition < STRUCTURE_DATA.size();
            }
        });
        when(tableMetaData.getString(anyString())).thenAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                if ("COLUMN_DEF".equals(args[0])) {
                    return STRUCTURE_DATA.get(resultSetPosition).columnDefaultValue;
                } else if ("COLUMN_NAME".equals(args[0])) {
                    return STRUCTURE_DATA.get(resultSetPosition).columnName;
                }
                return null;
            }
        });

        when(resultSetMetaData.getColumnCount()).thenReturn(STRUCTURE_DATA.size());
        when(resultSetMetaData.getColumnName(anyInt())).thenAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return STRUCTURE_DATA.get((Integer) args[0] - 1).columnName;
            }
        });
        when(resultSetMetaData.isNullable(anyInt())).thenAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return STRUCTURE_DATA.get((Integer) args[0] - 1).isNullable;
            }
        });
        when(resultSetMetaData.isAutoIncrement(anyInt())).thenAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return STRUCTURE_DATA.get((Integer) args[0] - 1).isAutoIncrement;
            }
        });
        when(resultSetMetaData.getColumnDisplaySize(anyInt())).thenAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return STRUCTURE_DATA.get((Integer) args[0] - 1).columnDisplaySize;
            }
        });
        when(resultSetMetaData.getColumnType(anyInt())).thenAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return STRUCTURE_DATA.get((Integer) args[0] - 1).columnType;
            }
        });
    }

    /**
     * The method tests that a DatabaseTableInfo object returns correct table's structure.
     * 
     * @throws SQLException
     *             Usually is thrown if there is an error during collaborating with the database. For the test should
     *             never happen.
     */
    @Test
    public void getStructureTest() throws SQLException {
        DbTable testObject = new DbTable(dataSource, "tableName");
        assertEquals(expectedStructure, testObject.getStructure());
        verify(resultSet).close();
        verify(statement).close();
        verify(tableMetaData).close();
        verify(connection).close();
    }

    /**
     * Helper-class to store and provide stub information about one table's row structure.
     * 
     * @author Evgeny Surovtsev
     * 
     */
    class ColumnStructure {
        /**
         * Construct and initializes an instance of TableStructureData with provided information.
         * 
         * @param columnName
         *            The name of the column.
         * @param columnType
         *            The JDBC's represent of the SQL type ({@link java.sql.Types}).
         * @param columnDisplaySize
         *            The column's display size.
         * @param columnDefaultValue
         *            The column's default value.
         * @param isAutoIncrement
         *            Does the column support autoincrementing.
         * @param isNullable
         *            Is column can be nullable.
         */
        ColumnStructure(final String columnName, final int columnType, final int columnDisplaySize,
                final String columnDefaultValue, final boolean isAutoIncrement, final int isNullable) {
            this.columnDefaultValue = columnDefaultValue;
            this.columnName = columnName;
            this.isAutoIncrement = isAutoIncrement;
            this.isNullable = isNullable;
            this.columnDisplaySize = columnDisplaySize;
            this.columnType = columnType;
        }

        String columnDefaultValue;
        String columnName;
        boolean isAutoIncrement;
        int isNullable;
        int columnDisplaySize;
        int columnType;
    }
}
