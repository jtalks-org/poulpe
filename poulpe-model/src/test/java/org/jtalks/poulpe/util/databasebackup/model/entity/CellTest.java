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
package org.jtalks.poulpe.util.databasebackup.model.entity;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.jtalks.poulpe.util.databasebackup.model.jdbc.SqlTypes;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Cell is mainly used in Collections so first of all we need to test it's comparison abilities.
 * 
 * @author Evgeny Surovtsev
 * 
 */
public class CellTest {
    private ColumnMetaData columnMetaData;
    private ColumnMetaData differentColumnMetaData;

    /**
     * Sets up a ColumnMetaData which is used in most of the tests and a different ColumnMetaData.
     */
    @BeforeClass
    protected void setUp() {
        columnMetaData = new ColumnMetaData("columnName", SqlTypes.VARCHAR);
        differentColumnMetaData = new ColumnMetaData("columnName", SqlTypes.INT);
    }

    /**
     * Two different instances of Cell should not be equal.
     */
    @Test
    public void twoNotEqualCellsAreNotEqual() {
        Cell testObject1 = new Cell(columnMetaData, "columnValue");
        Cell testObject2 = new Cell(differentColumnMetaData, "differentColumnValue");
        assertFalse(testObject1.equals(testObject2), "totally diferent objects");

        testObject1 = new Cell(columnMetaData, "columnValue");
        testObject2 = new Cell(differentColumnMetaData, "columnValue");
        assertFalse(testObject1.equals(testObject2), "only one field differs");

        testObject1 = new Cell(columnMetaData, "columnValue");
        testObject2 = new Cell(differentColumnMetaData, "columnValue");
        assertFalse(testObject1.equals(testObject2), "only one field differs");

        testObject1 = new Cell(columnMetaData, "columnValue");
        testObject2 = new Cell(columnMetaData, "differentColumnValue");
        assertFalse(testObject1.equals(testObject2), "only one field differs");
    }

    /**
     * Checks if two equal Cell with null as their values are still equal.
     */
    @Test
    public void twoEqualCellWithNullValueAreEqual() {
        Cell testObject1 = new Cell(columnMetaData, null);
        Cell testObject2 = new Cell(columnMetaData, null);
        assertEquals(testObject1, testObject2);
    }

    /**
     * Two the same instances of Cell should be equal.
     */
    @Test
    public void twoEqualCellsAreEqual() {
        Cell testObject1 = new Cell(columnMetaData, "columnValue");
        Cell testObject2 = new Cell(columnMetaData, "columnValue");
        assertEquals(testObject1, testObject2);
    }

    /**
     * Checks if creating instance with null parameters is forbidden.
     */
    @Test
    @SuppressWarnings("unused")
    public void instanceShouldBeInitializedWithNonNulls() {
        try {
            Cell testObject1 = new Cell(null, "columnValue");
            fail("columnName cannot be null.");
        } catch (NullPointerException e) {
            // do nothing - the exception is expected
        }
        try {
            Cell testObject1 = new Cell(null, null);
            fail("columnValue cannot be null.");
        } catch (NullPointerException e) {
            // do nothing - the exception is expected
        }
    }

    /**
     * Checks if a Tests Java Equals Contract is valid.
     */
    @Test
    public void equalsContractTest() {
        Cell testObject1 = new Cell(columnMetaData, "columnValue");
        assertTrue(testObject1.equals(testObject1), "Reflexive");

        Cell testObject2 = new Cell(columnMetaData, "columnValue");
        assertTrue(testObject1.equals(testObject2), "Equal Symmetric");
        assertTrue(testObject2.equals(testObject1), "Equal Symmetric");

        Cell diferentTestObject = new Cell(differentColumnMetaData, "columnValue");
        assertFalse(testObject1.equals(diferentTestObject), "Not Equal Symmetric");
        assertFalse(diferentTestObject.equals(testObject1), "Not Equal Symmetric");

        Cell testObject3 = new Cell(columnMetaData, "columnValue");
        assertTrue(testObject1.equals(testObject2), "Transitive");
        assertTrue(testObject1.equals(testObject3), "Transitive");
        assertTrue(testObject2.equals(testObject3), "Transitive");

        assertFalse(testObject2.equals(null), "Null value should return false");
    }
}
