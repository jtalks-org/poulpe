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
package org.jtalks.poulpe.model.databasebackup.jdbc;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.jtalks.poulpe.model.databasebackup.SqlTypes;
import org.jtalks.poulpe.model.databasebackup.dto.Cell;
import org.jtalks.poulpe.model.databasebackup.dto.ColumnMetaData;
import org.jtalks.poulpe.model.databasebackup.dto.ForeignKey;
import org.jtalks.poulpe.model.databasebackup.dto.Row;
import org.jtalks.poulpe.model.databasebackup.dto.UniqueKey;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;

/**
 * Tests fetching information from database functionality for the DbTable class.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class DbTableTest {
    private EmbeddedDatabase dataSource;

    /**
     * Sets up an in-memory database and initialize db schema by applying poulpe-model/src/test/resources/schema.sql.
     */
    @BeforeClass
    private void setUp() {
        dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL)
                .addScript("schema.sql").addScript("databasebackup_test_data.sql").build();
    }

    /**
     * Tests a DbTable.getPrimaryKeyList returns valid Primary Keys List. For selecting a table name and primary key for
     * the table see schema.sql. Table name and key name should be upper-case as seems HSQL upper-cases all given names.
     * 
     * @throws SQLException
     *             Usually is thrown if there is an error during collaborating with the database. For the test should
     *             never happen.
     */
    @Test
    public void getPrimaryKeyListTest() throws SQLException {
        List<UniqueKey> expectedPrimaryKeyList = Lists.newArrayList();
        expectedPrimaryKeyList.add(new UniqueKey("VERSION"));
        assertEquals(new DbTable(dataSource, "POULPE_SCHEMA_VERSION").getPrimaryKeyList(), expectedPrimaryKeyList);
    }

    /**
     * Tests a DbTable.getPrimaryKeyList returns valid Primary Keys List. For selecting a table name and primary key for
     * the table see schema.sql. Table name and key name should be upper-case as seems HSQL upper-cases all given names.
     * 
     * @throws SQLException
     *             Usually is thrown if there is an error during collaborating with the database. For the test should
     *             never happen.
     */
    @Test
    public void getUniqueKeyListTest() throws SQLException {
        List<UniqueKey> expectedUniqueKeyList = Lists.newArrayList();
        expectedUniqueKeyList.add(new UniqueKey("UUID"));
        expectedUniqueKeyList.add(new UniqueKey("USERNAME"));
        expectedUniqueKeyList.add(new UniqueKey("EMAIL"));
        assertEquals(new DbTable(dataSource, "USERS").getUniqueKeyList(), expectedUniqueKeyList);
    }

    /**
     * Tests a DbTable.getForeignKeyList returns valid Foreign Keys List. For selecting a table name and foreign keys
     * for the table see schema.sql. Table name and key names should be upper-case as seems HSQL upper-cases all given
     * names.
     * 
     * @throws SQLException
     *             Usually is thrown if there is an error during collaborating with the database. For the test should
     *             never happen.
     */
    @Test
    public void getForeignKeyListTest() throws SQLException {
        List<ForeignKey> expectedForeignKeyList = Lists.newArrayList();
        expectedForeignKeyList.add(new ForeignKey("FK_ACL_OBJ_CLASS", "OBJECT_ID_CLASS", "ACL_CLASS", "ID"));
        expectedForeignKeyList.add(
                new ForeignKey("FK_ACL_OBJ_PARENT", "PARENT_OBJECT", "ACL_OBJECT_IDENTITY", "ID"));
        expectedForeignKeyList.add(new ForeignKey("FK_ACL_OBJ_OWNER", "OWNER_SID", "ACL_SID", "ID"));

        assertEquals(new DbTable(dataSource, "ACL_OBJECT_IDENTITY").getForeignKeyList(), expectedForeignKeyList);
    }

    /**
     * The method tests that a DbTable object returns correct data for the table.
     * 
     * @throws SQLException
     *             Usually is thrown if there is an error during collaborating with the database. For the test should
     *             never happen.
     */
    @Test(enabled = false)
    public void getDataTest() throws SQLException {
        // Define expected table structure.
        Map<String, ColumnMetaData> metaColumnInfoMap = Maps.newHashMap();
        metaColumnInfoMap.put("EXECUTION_TIME", new ColumnMetaData("EXECUTION_TIME", SqlTypes.INT));
        metaColumnInfoMap.put("SCRIPT", new ColumnMetaData("SCRIPT", SqlTypes.VARCHAR));
        metaColumnInfoMap.put("INSTALLED_ON", new ColumnMetaData("INSTALLED_ON", SqlTypes.TIMESTAMP));
        metaColumnInfoMap.put("STATE", new ColumnMetaData("STATE", SqlTypes.VARCHAR));
        metaColumnInfoMap.put("VERSION", new ColumnMetaData("VERSION", SqlTypes.VARCHAR));
        metaColumnInfoMap.put("DESCRIPTION", new ColumnMetaData("DESCRIPTION", SqlTypes.VARCHAR));
        metaColumnInfoMap.put("CURRENT_VERSION", new ColumnMetaData("CURRENT_VERSION", SqlTypes.TINYINT));
        metaColumnInfoMap.put("CHECKSUM", new ColumnMetaData("CHECKSUM", SqlTypes.INT));
        metaColumnInfoMap.put("TYPE", new ColumnMetaData("TYPE", SqlTypes.VARCHAR));
        metaColumnInfoMap.put("INSTALLED_BY", new ColumnMetaData("INSTALLED_BY", SqlTypes.VARCHAR));
        // Define expected table data
        List<Row> expectedResult = Lists.newArrayList();
        expectedResult.add(new Row()
                .addCell(new Cell(metaColumnInfoMap.get("EXECUTION_TIME"), new Integer(229)))
                .addCell(new Cell(metaColumnInfoMap.get("SCRIPT"), "V12__Moderators_Group_Column_In_Branches.sql"))
                .addCell(new Cell(metaColumnInfoMap.get("INSTALLED_ON"), new Timestamp(1349881441000L)))
                .addCell(new Cell(metaColumnInfoMap.get("STATE"), "SUCCESS"))
                .addCell(new Cell(metaColumnInfoMap.get("VERSION"), "12"))
                .addCell(new Cell(metaColumnInfoMap.get("DESCRIPTION"), "Moderators Group Column In Branches"))
                .addCell(new Cell(metaColumnInfoMap.get("CURRENT_VERSION"), new Integer(0)))
                .addCell(new Cell(metaColumnInfoMap.get("CHECKSUM"), new Integer(140599915)))
                .addCell(new Cell(metaColumnInfoMap.get("TYPE"), "SQL"))
                .addCell(new Cell(metaColumnInfoMap.get("INSTALLED_BY"), "root")));
        expectedResult.add(new Row()
                .addCell(new Cell(metaColumnInfoMap.get("EXECUTION_TIME"), new Integer(140)))
                .addCell(new Cell(metaColumnInfoMap.get("SCRIPT"), "V13__Branch_Table_Column_Type_Change.sql"))
                .addCell(new Cell(metaColumnInfoMap.get("INSTALLED_ON"), new Timestamp(1349881441000L)))
                .addCell(new Cell(metaColumnInfoMap.get("STATE"), "SUCCESS"))
                .addCell(new Cell(metaColumnInfoMap.get("VERSION"), "13"))
                .addCell(new Cell(metaColumnInfoMap.get("DESCRIPTION"), "Branch Table Column Type Change"))
                .addCell(new Cell(metaColumnInfoMap.get("CURRENT_VERSION"), new Integer(0)))
                .addCell(new Cell(metaColumnInfoMap.get("CHECKSUM"), new Integer(1899329008)))
                .addCell(new Cell(metaColumnInfoMap.get("TYPE"), "SQL"))
                .addCell(new Cell(metaColumnInfoMap.get("INSTALLED_BY"), "root")));
        expectedResult.add(new Row()
                .addCell(new Cell(metaColumnInfoMap.get("EXECUTION_TIME"), new Integer(232)))
                .addCell(new Cell(metaColumnInfoMap.get("SCRIPT"), "V14__Users_Table_Unnecessary_columns.sql"))
                .addCell(new Cell(metaColumnInfoMap.get("INSTALLED_ON"), new Timestamp(1349881442000L)))
                .addCell(new Cell(metaColumnInfoMap.get("STATE"), "SUCCESS"))
                .addCell(new Cell(metaColumnInfoMap.get("VERSION"), "14"))
                .addCell(new Cell(metaColumnInfoMap.get("DESCRIPTION"), "Users Table' \"Unnecessary columns"))
                .addCell(new Cell(metaColumnInfoMap.get("CURRENT_VERSION"), new Integer(0)))
                .addCell(new Cell(metaColumnInfoMap.get("CHECKSUM"), null))
                .addCell(new Cell(metaColumnInfoMap.get("TYPE"), "SQL"))
                .addCell(new Cell(metaColumnInfoMap.get("INSTALLED_BY"), "root")));

        List<Row> actualResult = new DbTable(dataSource, "common_schema_version").getData();
        assertEquals(actualResult.size(), expectedResult.size());

        // Each row must have the same count of cells
        int expectedColumnCount = actualResult.get(0).getCellCount();
        for (Row actualResultRow : actualResult) {
            assertEquals(actualResultRow.getCellCount(), expectedColumnCount);
        }

        // check each row in the list
        for (int i = 0; i < actualResult.size(); i++) {
            Row expectedRow = expectedResult.get(i);
            Row actualRow = actualResult.get(i);
            assertEquals(actualRow.getCellCount(), expectedRow.getCellCount());
            // check each cell in the row
            for (Cell expectedCell : expectedRow.getCellList()) {
                assertTrue(actualRow.getCellList().contains(expectedCell), "Expected " + expectedCell
                        + " is not found in actual " + actualRow.getCellList());
            }
        }
    }

    /**
     * The method tests that a DbTable object returns correct table's structure.
     * 
     * @throws SQLException
     *             Usually is thrown if there is an error during collaborating with the database. For the test should
     *             never happen.
     */
    @Test
    public void getStructureTest() throws SQLException {
        List<ColumnMetaData> expectedResult = Lists.newArrayList();
        expectedResult.add(new ColumnMetaData("EXECUTION_TIME", SqlTypes.INT).setNullable(true).setSize(11));
        expectedResult.add(new ColumnMetaData("SCRIPT", SqlTypes.VARCHAR).setSize(200));
        expectedResult.add(new ColumnMetaData("INSTALLED_ON", SqlTypes.TIMESTAMP));
        expectedResult.add(new ColumnMetaData("STATE", SqlTypes.VARCHAR).setSize(15));
        expectedResult.add(new ColumnMetaData("VERSION", SqlTypes.VARCHAR).setSize(20));
        expectedResult.add(new ColumnMetaData("DESCRIPTION", SqlTypes.VARCHAR).setNullable(true).setSize(100));
        expectedResult.add(new ColumnMetaData("CURRENT_VERSION", SqlTypes.TINYINT).setSize(4));
        expectedResult.add(new ColumnMetaData("CHECKSUM", SqlTypes.INT).setNullable(true).setSize(11));
        expectedResult.add(new ColumnMetaData("TYPE", SqlTypes.VARCHAR).setSize(10));
        expectedResult.add(new ColumnMetaData("INSTALLED_BY", SqlTypes.VARCHAR).setSize(30));

        DbTable testObject = new DbTable(dataSource, "common_schema_version");
        List<ColumnMetaData> actualResult = testObject.getStructure();

        assertEquals(actualResult.size(), expectedResult.size());
        for (ColumnMetaData actualColumnMetaData : actualResult) {
            assertTrue(expectedResult.contains(actualColumnMetaData), actualColumnMetaData + " is no found in "
                    + expectedResult);
        }
    }

    /**
     * Closes previously opened resources such as database connection.
     */
    @AfterClass
    public void tearDown() {
        dataSource.shutdown();
    }
}
