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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.jtalks.poulpe.util.databasebackup.domain.Cell;
import org.jtalks.poulpe.util.databasebackup.domain.ColumnMetaData;
import org.jtalks.poulpe.util.databasebackup.domain.ForeignKey;
import org.jtalks.poulpe.util.databasebackup.domain.Row;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

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
    @BeforeClass(groups = { "databasebackup" })
    protected void setUp() {
        dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("schema.sql")
                .addScript("databasebackup_test_data.sql")
                .build();
    }

    /**
     * Tests a DbTable.getPrimaryKeyList returns valid Primary Keys List. For selecting a table name and primary key for
     * the table see schema.sql. Table name and key name should be upper-case as seems HSQL upper-cases all given names.
     * 
     * @throws SQLException
     *             Usually is thrown if there is an error during collaborating with the database. For the test should
     *             never happen.
     */
    @Test(groups = { "databasebackup" })
    public void getPrimaryKeySetTest() throws SQLException {
        DbTable testObject = new DbTable(dataSource, "POULPE_SCHEMA_VERSION");

        Assert.assertEquals(1, testObject.getPrimaryKeySet().size());
        Assert.assertEquals(1, testObject.getPrimaryKeySet().iterator().next().getColumnNameSet().size());
        assertTrue(testObject.getPrimaryKeySet().iterator().next().getColumnNameSet().contains("VERSION"));
    }

    /**
     * Tests a DbTable.getPrimaryKeyList returns valid Primary Keys List. For selecting a table name and primary key for
     * the table see schema.sql. Table name and key name should be upper-case as seems HSQL upper-cases all given names.
     * 
     * @throws SQLException
     *             Usually is thrown if there is an error during collaborating with the database. For the test should
     *             never happen.
     */
    // @Test(groups = {"databasebackup"})
    // public void getUniqueKeyListTest() throws SQLException {
    // Set<UniqueKey> expectedUniqueKeySet =
    // Sets.newHashSet(
    // new UniqueKey("UUID", "UUID"),
    // new UniqueKey("USERNAME", "USERNAME"),
    // new UniqueKey("EMAIL", "EMAIL"));
    // DbTable testObject = new DbTable(dataSource, "USERS");
    //
    // Assert.assertEquals(3, testObject.getUniqueKeySet().size());
    // // assertEquals(testObject.getUniqueKeySet(), expectedUniqueKeySet);
    // }

    /**
     * Composite unique key (like CONSTRAINT uk_acl_sid UNIQUE (sid, principal)) must be composed into one constraint
     * (as in the example).
     * 
     * @throws SQLException
     *             must never happen.
     */
    // @Test(groups = {"databasebackup"})
    // public void compositeUniqueKeysShouldBeCollectedIntoOneConstraint() throws SQLException {
    // Set<UniqueKey> expectedUniqueKeySet =
    // Sets.newHashSet(new UniqueKey("uk_acl_sid", Sets.newHashSet("sid", "principal")));
    // DbTable testObject = new DbTable(dataSource, "ACL_SID");
    //
    // assertEquals(testObject.getUniqueKeySet(), expectedUniqueKeySet);
    // }

    /**
     * Tests a DbTable.getForeignKeyList returns valid Foreign Keys List. For selecting a table name and foreign keys
     * for the table see schema.sql. Table name and key names should be upper-case as seems HSQL upper-cases all given
     * names.
     * 
     * @throws SQLException
     *             Usually is thrown if there is an error during collaborating with the database. For the test should
     *             never happen.
     */
    @Test(groups = { "databasebackup" })
    public void getForeignKeySetTest() throws SQLException {
        Set<ForeignKey> expectedForeignKeySet = Sets.newHashSet(
                new ForeignKey("FK_ACL_OBJ_CLASS", "OBJECT_ID_CLASS", "ACL_CLASS", "ID"),
                new ForeignKey("FK_ACL_OBJ_PARENT", "PARENT_OBJECT", "ACL_OBJECT_IDENTITY", "ID"),
                new ForeignKey("FK_ACL_OBJ_OWNER", "OWNER_SID", "ACL_SID", "ID"));
        DbTable testObject = new DbTable(dataSource, "ACL_OBJECT_IDENTITY");

        assertEquals(testObject.getForeignKeySet(), expectedForeignKeySet);
    }

    /**
     * The method tests that a DbTable object returns correct data for the table.
     * 
     * @throws SQLException
     *             Usually is thrown if there is an error during collaborating with the database. For the test should
     *             never happen.
     */
    @Test(enabled = false, groups = { "databasebackup" })
    public void getDataTest() throws SQLException {
        // Define expected table structure.
        Map<String, ColumnMetaData> metaColumnInfoMap = Maps.newHashMap();
        metaColumnInfoMap.put("EXECUTION_TIME", ColumnMetaData.getInstance("EXECUTION_TIME", SqlTypes.INT));
        metaColumnInfoMap.put("SCRIPT", ColumnMetaData.getInstance("SCRIPT", SqlTypes.VARCHAR));
        metaColumnInfoMap.put("INSTALLED_ON", ColumnMetaData.getInstance("INSTALLED_ON", SqlTypes.TIMESTAMP));
        metaColumnInfoMap.put("STATE", ColumnMetaData.getInstance("STATE", SqlTypes.VARCHAR));
        metaColumnInfoMap.put("VERSION", ColumnMetaData.getInstance("VERSION", SqlTypes.VARCHAR));
        metaColumnInfoMap.put("DESCRIPTION", ColumnMetaData.getInstance("DESCRIPTION", SqlTypes.VARCHAR));
        metaColumnInfoMap.put("CURRENT_VERSION", ColumnMetaData.getInstance("CURRENT_VERSION", SqlTypes.TINYINT));
        metaColumnInfoMap.put("CHECKSUM", ColumnMetaData.getInstance("CHECKSUM", SqlTypes.INT));
        metaColumnInfoMap.put("TYPE", ColumnMetaData.getInstance("TYPE", SqlTypes.VARCHAR));
        metaColumnInfoMap.put("INSTALLED_BY", ColumnMetaData.getInstance("INSTALLED_BY", SqlTypes.VARCHAR));

        // Define expected table data
        List<Row> expectedResult = Lists.newArrayList(
                new Row()
                        .addCell(new Cell(metaColumnInfoMap.get("EXECUTION_TIME"), new Integer(229)))
                        .addCell(
                                new Cell(metaColumnInfoMap.get("SCRIPT"),
                                        "V12__Moderators_Group_Column_In_Branches.sql"))
                        .addCell(new Cell(metaColumnInfoMap.get("INSTALLED_ON"), new Timestamp(1349881441000L)))
                        .addCell(new Cell(metaColumnInfoMap.get("STATE"), "SUCCESS"))
                        .addCell(new Cell(metaColumnInfoMap.get("VERSION"), "12"))
                        .addCell(new Cell(metaColumnInfoMap.get("DESCRIPTION"), "Moderators Group Column In Branches"))
                        .addCell(new Cell(metaColumnInfoMap.get("CURRENT_VERSION"), new Integer(0)))
                        .addCell(new Cell(metaColumnInfoMap.get("CHECKSUM"), new Integer(140599915)))
                        .addCell(new Cell(metaColumnInfoMap.get("TYPE"), "SQL"))
                        .addCell(new Cell(metaColumnInfoMap.get("INSTALLED_BY"), "root")),
                new Row()
                        .addCell(new Cell(metaColumnInfoMap.get("EXECUTION_TIME"), new Integer(140)))
                        .addCell(new Cell(metaColumnInfoMap.get("SCRIPT"), "V13__Branch_Table_Column_Type_Change.sql"))
                        .addCell(new Cell(metaColumnInfoMap.get("INSTALLED_ON"), new Timestamp(1349881441000L)))
                        .addCell(new Cell(metaColumnInfoMap.get("STATE"), "SUCCESS"))
                        .addCell(new Cell(metaColumnInfoMap.get("VERSION"), "13"))
                        .addCell(new Cell(metaColumnInfoMap.get("DESCRIPTION"), "Branch Table Column Type Change"))
                        .addCell(new Cell(metaColumnInfoMap.get("CURRENT_VERSION"), new Integer(0)))
                        .addCell(new Cell(metaColumnInfoMap.get("CHECKSUM"), new Integer(1899329008)))
                        .addCell(new Cell(metaColumnInfoMap.get("TYPE"), "SQL"))
                        .addCell(new Cell(metaColumnInfoMap.get("INSTALLED_BY"), "root")),
                new Row()
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
    @Test(groups = { "databasebackup" })
    public void getStructureTest() throws SQLException {
        List<ColumnMetaData> expectedResult = Lists.newArrayList(
                ColumnMetaData.getInstance("EXECUTION_TIME", SqlTypes.INT).setNullable(true).setSize(11),
                ColumnMetaData.getInstance("SCRIPT", SqlTypes.VARCHAR).setSize(200),
                ColumnMetaData.getInstance("INSTALLED_ON", SqlTypes.TIMESTAMP),
                ColumnMetaData.getInstance("STATE", SqlTypes.VARCHAR).setSize(15),
                ColumnMetaData.getInstance("VERSION", SqlTypes.VARCHAR).setSize(20),
                ColumnMetaData.getInstance("DESCRIPTION", SqlTypes.VARCHAR).setNullable(true).setSize(100),
                ColumnMetaData.getInstance("CURRENT_VERSION", SqlTypes.TINYINT).setSize(4),
                ColumnMetaData.getInstance("CHECKSUM", SqlTypes.INT).setNullable(true).setSize(11),
                ColumnMetaData.getInstance("TYPE", SqlTypes.VARCHAR).setSize(10),
                ColumnMetaData.getInstance("INSTALLED_BY", SqlTypes.VARCHAR).setSize(30));

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
    @AfterClass(groups = { "databasebackup" })
    public void tearDown() {
        dataSource.shutdown();
    }
}
