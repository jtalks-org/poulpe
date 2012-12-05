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

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.sql.DataSource;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.collect.Maps;

/**
 * Tests getting common parameters functionality for the DbTable class.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class DbTableCommonParametersTest {

    private final DataSource dataSource = mock(DataSource.class);
    private final Connection connection = mock(Connection.class);
    private final Statement statement = mock(Statement.class);
    private final ResultSet resultSet = mock(ResultSet.class);

    /**
     * Current position in ResultSet is stored in resultSetPosition (with initial position before start of the records
     * so first ResultSet.next() moves position pointer to the first (0) position).
     */
    private int resultSetPosition = -1;
    private static final Map<String, String> COMMON_PARAMETERS_DATA = Maps.newHashMap();
    private static Map<String, String> expectedCommonParameterMap;

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
    protected void setupStubData() throws SQLException {
        COMMON_PARAMETERS_DATA.put("Engine", "engineParameter");
        COMMON_PARAMETERS_DATA.put("Collation", "collationParameter");
        COMMON_PARAMETERS_DATA.put("Auto_increment", "autoIncrementParameter");

        /*
         * Define mock objects
         */
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeQuery(anyString())).thenReturn(resultSet);

        when(resultSet.next()).thenAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) {
                return ++resultSetPosition < COMMON_PARAMETERS_DATA.size();
            }
        });
        when(resultSet.getString(anyString())).thenAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return COMMON_PARAMETERS_DATA.get(args[0]);
            }
        });

        /*
         * Prepare expected result.
         */
        expectedCommonParameterMap = Maps.newHashMap();
        expectedCommonParameterMap.put("COLLATE", COMMON_PARAMETERS_DATA.get("Collation"));
        expectedCommonParameterMap.put("ENGINE", COMMON_PARAMETERS_DATA.get("Engine"));
        expectedCommonParameterMap.put("AUTO_INCREMENT", COMMON_PARAMETERS_DATA.get("Auto_increment"));
    }

    /**
     * The method tests that a DatabaseTableInfo object returns correct common parameters for the table.
     * 
     * @throws SQLException
     *             Usually is thrown if there is an error during collaborating with the database. For the test should
     *             never happen.
     */
    @Test
    public void getCommonParameterMapTest() throws SQLException {
        DbTable testObject = new DbTable(dataSource, "tableName");
        assertEquals(testObject.getCommonParameterMap(), expectedCommonParameterMap);
        verify(statement).close();
        verify(resultSet).close();
        verify(connection).close();
    }
}
